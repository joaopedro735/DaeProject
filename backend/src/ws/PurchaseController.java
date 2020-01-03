package ws;

import dtos.PurchaseDTO;
import ejbs.ProductBean;
import ejbs.ProductPurchaseBean;
import ejbs.PurchaseBean;
import ejbs.UserBean;
import entities.*;
import exceptions.MyConstraintViolationException;
import exceptions.MyEntityAlreadyExistsException;
import exceptions.MyEntityNotFoundException;

import javax.ejb.EJB;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.security.Principal;
import java.util.*;

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

        if(securityContext.isUserInRole("Administrator")){
            Set<ProductPurchase> productPurchases = new HashSet<>();
            Product product;
            ProductPurchase productPurchase;
            Purchase purchase;

            for (int i = 0; i < purchaseDTO.getProductPurchases().length; i++) {
                product = productBean.find(purchaseDTO.getProductPurchases()[i].getProduct().getId());
                if (product == null){
                    throw new MyEntityNotFoundException("Product with id " + purchaseDTO.getProductPurchases()[i].getProduct().getId() + " not found");
                }
                productPurchase = productPurchaseBean.create(product, purchaseDTO.getProductPurchases()[i].getUnity(), purchaseDTO.getProductPurchases()[i].getQuantity());
                productPurchases.add(productPurchase);
                //totalEuros += product.getValue() * purchaseDTO.getProductPurchases()[i].getQuantity();
            }
           purchase = purchaseBean.create(productPurchases, purchaseDTO.getUsername());
        return Response.status(Response.Status.OK).entity(toDTO(purchase)).build();
        }
        return Response.status(Response.Status.FORBIDDEN).build();
    }

   /* @POST
    @Path("/{id}/payment")
    public Response createPaymentPurchase(@PathParam("id") int id, PaymentDTO paymentDTO){

    }*/
}
