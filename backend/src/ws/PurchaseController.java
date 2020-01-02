package ws;

import dtos.ProductDTO;
import dtos.PurchaseDTO;
import ejbs.ProductBean;
import ejbs.ProductPurchaseBean;
import ejbs.PurchaseBean;
import ejbs.UserBean;
import entities.Product;
import entities.ProductPurchase;
import entities.Purchase;
import entities.User;
import exceptions.MyConstraintViolationException;
import exceptions.MyEntityAlreadyExistsException;
import exceptions.MyEntityNotFoundException;

import javax.ejb.EJB;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.lang.reflect.Array;
import java.security.Principal;
import java.util.*;
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

    public static PurchaseDTO toDTO(Purchase purchase) {
        return new PurchaseDTO(
                purchase.getId(),
                purchase.getPurchaseDate(),
                purchase.getUser().getUsername(),
                purchase.getTotalEuros()
        );
    }

    public static List<ProductDTO> toDTOs(Collection<Product> products) {
        return products.stream().map(ws.ProductController::toDTO).collect(Collectors.toList());
    }

    @POST
    @Path("/")
    public Response createNewPurchase(PurchaseDTO purchaseDTO) throws MyEntityAlreadyExistsException, MyEntityNotFoundException, MyConstraintViolationException {
        Principal principal = securityContext.getUserPrincipal();
        if(securityContext.isUserInRole("Administrator")){
            Set<ProductPurchase> productPurchases = new HashSet<ProductPurchase>();
            Product product;
            ProductPurchase productPurchase;
            Purchase purchase;
            float totalEuros = 0;

            for (int i = 0; i < purchaseDTO.getProductPurchases().length; i++) {
                product = productBean.find(purchaseDTO.getProductPurchases()[i].getProduct().getId());
                if (product == null){
                    throw new MyEntityNotFoundException("Product with id " + purchaseDTO.getProductPurchases()[i].getProduct().getId() + " not found");
                }
                productPurchase = productPurchaseBean.create(product, purchaseDTO.getProductPurchases()[i].getUnity(), purchaseDTO.getProductPurchases()[i].getQuantity());
                productPurchases.add(productPurchase);
                totalEuros += product.getValue() * purchaseDTO.getProductPurchases()[i].getQuantity();
            }
           purchase = purchaseBean.create(productPurchases, purchaseDTO.getUsername(), totalEuros);
        return Response.status(Response.Status.OK).entity(toDTO(purchase)).build();
        }
        return Response.status(Response.Status.FORBIDDEN).build();
    }
}
