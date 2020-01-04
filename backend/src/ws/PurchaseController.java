package ws;

import dtos.AdministratorDTO;
import dtos.ProductPurchaseDTO;
import dtos.PurchaseDTO;
import dtos.SportDTO;
import ejbs.ProductBean;
import ejbs.ProductPurchaseBean;
import ejbs.PurchaseBean;
import ejbs.UserBean;
import entities.Administrator;
import entities.Product;
import entities.ProductPurchase;
import entities.Purchase;
import exceptions.MyConstraintViolationException;
import exceptions.MyEntityAlreadyExistsException;
import exceptions.MyEntityNotFoundException;

import javax.ejb.EJB;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.security.Principal;
import java.text.SimpleDateFormat;
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
                purchase.getTotalEuros().doubleValue()
        );

        if (fn != null) {
            return fn.apply(dto);
        }
        return dto;
    }

    public static ProductPurchaseDTO ProdsPurchasestoDTO(ProductPurchase productPurchase) {
        return new ProductPurchaseDTO(
                productPurchase.getId(),
                productPurchase.getProduct().getType().getId(),
                productPurchase.getProduct().getType().getName(),
                productPurchase.getProduct().getDescription(),
                productPurchase.getUnity(),
                productPurchase.getQuantity()
        );
    }

    public static List<PurchaseDTO> toDTOs(List<Purchase> purchases, Function<PurchaseDTO, PurchaseDTO> fn) {
        return purchases.stream().map(s -> PurchaseController.toDTO(s, fn)).collect(Collectors.toList());
    }

    public static List<ProductPurchaseDTO> ProdsPurchasestoDTOs(Collection<ProductPurchase> productPurchases) {
        return productPurchases.stream().map(PurchaseController::ProdsPurchasestoDTO).collect(Collectors.toList());
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
        System.out.println(principal.getName());
        if (securityContext.isUserInRole("Administrator") || principal.getName().equals(id)) {
            Purchase purchase = purchaseBean.find(id);
            return Response.status(Response.Status.OK).entity(toDTO(purchase, dto -> {
                dto.setProductPurchases((ProductPurchaseDTO[]) PurchaseController.ProdsPurchasestoDTOs(purchase.getProductPurchases()).toArray());
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

   /* @POST
    @Path("/{id}/payment")
    public Response createPaymentPurchase(@PathParam("id") int id, PaymentDTO paymentDTO){

    }*/
}
