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
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
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
                purchase.getPaymentListIDs(),
                purchase.getProductPurchasesListIDs(),
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
            ArrayList arrayProductPurchasesIDs = purchaseDTO.getProductPurchasesIDs();
            Set<ProductPurchase> productPurchaseSet = null;
            for (Object id: arrayProductPurchasesIDs) {
                productPurchaseSet.add(productPurchaseBean.find((int) id));
            }

            Purchase purchase = purchaseBean.create(
                productPurchaseSet, purchaseDTO.getUsername(), purchaseDTO.getTotalEuros()
            );
            return Response.status(Response.Status.CREATED)
                    .entity(toDTO(purchase))
                    .build();
        }
        return Response.status(Response.Status.FORBIDDEN).build();
    }
}
