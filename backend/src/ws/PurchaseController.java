package ws;

import dtos.ProductPurchaseDTO;
import dtos.PurchaseDTO;
import dtos.*;
import ejbs.ProductBean;
import ejbs.ProductPurchaseBean;
import ejbs.PurchaseBean;
import ejbs.UserBean;
import entities.*;
import exceptions.MyConstraintViolationException;
import exceptions.MyEntityAlreadyExistsException;
import exceptions.MyEntityIllegalArgumentException;
import exceptions.MyEntityNotFoundException;

import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.security.Principal;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Path("/purchases")
@Produces({MediaType.APPLICATION_JSON})
@Consumes({MediaType.APPLICATION_JSON})

public class PurchaseController {
    @EJB
    private ProductBean productBean;

    @EJB
    private PurchaseBean purchaseBean;

    @EJB
    private ProductPurchaseBean productPurchaseBean;

    @EJB
    private UserBean userBean;

    @Context
    private SecurityContext securityContext;

    public static PurchaseDTO toDTO(Purchase purchase, Function<PurchaseDTO, PurchaseDTO> fn) {
        PurchaseDTO dto = new PurchaseDTO(
                purchase.getId(),
                purchase.getPurchaseDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy hh:mm:ss")),
                purchase.getUser().getUsername(),
                purchase.getTotalEuros().doubleValue(),
                purchase.getUser().getName()
        );

        if (fn != null) {
            return fn.apply(dto);
        }
        return dto;
    }

    public static ProductPurchaseDTO prodsPurchasesToDTO(ProductPurchase productPurchase) {
        return new ProductPurchaseDTO(
                productPurchase.getId(),
                productPurchase.getProduct().getId(),
                productPurchase.getProduct().getType().getId(),
                productPurchase.getProduct().getType().getName(),
                productPurchase.getProduct().getDescription(),
                productPurchase.getUnity(),
                productPurchase.getQuantity()
        );
    }

    public static PaymentDTO paymentToDTO(Payment payment) {
        return new PaymentDTO(
                payment.getId(),
                payment.getDatePayment().toString(),
                payment.getLimitDayPayment().toString(),
                payment.getState().toString(),
                payment.getPaymentMethod(),
                payment.getValue()
        );
    }

    public static List<PurchaseDTO> toDTOs(List<Purchase> purchases, Function<PurchaseDTO, PurchaseDTO> fn) {
        return purchases.stream().map(s -> PurchaseController.toDTO(s, fn)).collect(Collectors.toList());
    }

    public static List<PaymentDTO> paymentToDTOs(Collection<Payment> purchases) {
        return purchases.stream().map(PurchaseController::paymentToDTO).collect(Collectors.toList());
    }

    public static ProductPurchaseDTO[] prodsPurchasestoDTOs(Collection<ProductPurchase> productPurchases) {
        return productPurchases.stream().map(PurchaseController::prodsPurchasesToDTO).toArray(ProductPurchaseDTO[]::new);
    }

    @GET
    @Path("/")
    public Response all() {
        String msg;
        try {
            GenericEntity<List<PurchaseDTO>> entity = new GenericEntity<List<PurchaseDTO>>(toDTOs(purchaseBean.all(), null)) {
            };
            return Response.status(Response.Status.OK)
                    .entity(entity)
                    .build();
        } catch (Exception e) {
            msg = "ERROR_GET_PURCHASES --->" + e.getMessage();
        }
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(msg)
                .build();
    }

    @GET
    @Path("/{id}")
    public Response getPurchaseDetails(@PathParam("id") int id) {
        Principal principal = securityContext.getUserPrincipal();
        if (principal != null && (securityContext.isUserInRole("Administrator") || principal.getName().equals(id))) {
            Purchase purchase = purchaseBean.find(id);
            return Response.status(Response.Status.OK).entity(toDTO(purchase, dto -> {
                dto.setProductPurchases(PurchaseController.prodsPurchasestoDTOs(purchase.getProductPurchases()));
                dto.setPayments(PurchaseController.paymentToDTOs(purchase.getPaymentList()));
                return dto;
            })).build();
        }
        return Response.status(Response.Status.FORBIDDEN).build();
    }

    @POST
    @Path("/")
    public Response createNewPurchase(PurchaseDTO purchaseDTO) throws MyEntityAlreadyExistsException, MyEntityNotFoundException, MyConstraintViolationException {
        Principal principal = securityContext.getUserPrincipal();

        if (securityContext.isUserInRole("Administrator")) {
            Set<ProductPurchase> productPurchases = new HashSet<>();
            for (ProductPurchaseDTO productPurchaseDTO : purchaseDTO.getProductPurchases()) {
                Product product = productBean.find(productPurchaseDTO.getProduct().getId());
                if (product == null) {
                    throw new MyEntityNotFoundException("Product with id " + productPurchaseDTO.getProduct().getId() + " not found");
                }
                ProductPurchase productPurchase = productPurchaseBean.create(product, productPurchaseDTO.getUnity(), productPurchaseDTO.getQuantity());
                productPurchases.add(productPurchase);
            }
            Purchase purchase = purchaseBean.create(productPurchases, purchaseDTO.getUsername());
            return Response.status(Response.Status.OK).entity(toDTO(purchase, null)).build();
        }
        return Response.status(Response.Status.FORBIDDEN).build();
    }

    @POST
    @Path("/{id}/payment")
    public Response createPaymentPurchase(@PathParam("id") int id, PaymentDTO paymentDTO) throws MyEntityNotFoundException, MyEntityIllegalArgumentException {
        Principal principal = securityContext.getUserPrincipal();
        if (securityContext.isUserInRole("Administrator")) {
            Purchase purchase = purchaseBean.find(id);
            if (purchase == null){
                throw new MyEntityNotFoundException("Purchase with that id " + id + "not found");
            }

            Payment payment = purchaseBean.addPayment(id, paymentDTO.getLimitDayPayment(), paymentDTO.getPaymentMethod(), paymentDTO.getValue());

            return Response.status(Response.Status.OK).entity(paymentToDTO(payment)).build();
        }
        return Response.status(Response.Status.FORBIDDEN).build();
    }
}
