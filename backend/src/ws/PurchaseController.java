package ws;

import dtos.ProductPurchaseDTO;
import dtos.PurchaseDTO;
import ejbs.ProductBean;
import ejbs.ProductPurchaseBean;
import ejbs.PurchaseBean;
import ejbs.UserBean;
import entities.Product;
import entities.ProductPurchase;
import entities.Purchase;
import exceptions.MyConstraintViolationException;
import exceptions.MyEntityAlreadyExistsException;
import exceptions.MyEntityNotFoundException;

import javax.ejb.EJB;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import java.security.Principal;
import java.util.HashSet;
import java.util.Set;

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

    public static PurchaseDTO toDTO(Purchase purchase) {
        return new PurchaseDTO(
                purchase.getId(),
                purchase.getPurchaseDate(),
                purchase.getUser().getUsername(),
                purchase.getTotalEuros().doubleValue()
        );
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
            return Response.status(Response.Status.OK).entity(toDTO(purchase)).build();
        }
        return Response.status(Response.Status.FORBIDDEN).build();
    }

   /* @POST
    @Path("/{id}/payment")
    public Response createPaymentPurchase(@PathParam("id") int id, PaymentDTO paymentDTO){

    }*/
}
