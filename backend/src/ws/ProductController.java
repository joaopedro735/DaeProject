package ws;

import dtos.ProductDTO;
import ejbs.ProductBean;
import entities.Product;
import exceptions.MyConstraintViolationException;
import exceptions.MyEntityAlreadyExistsException;
import exceptions.MyEntityNotFoundException;

import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.security.Principal;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Path("/products")
@Produces({MediaType.APPLICATION_JSON})
@Consumes({MediaType.APPLICATION_JSON})
@RolesAllowed("Administrator")
public class ProductController {
    @EJB
    private ProductBean productBean;

    @Context
    private SecurityContext securityContext;

    public static ProductDTO toDTO(Product product) {
        return new ProductDTO(
                product.getId(),
                product.getDescription(),
                product.getType().getId(),
                product.getValue(),
                product.getType().getName()
        );
    }

    public static List<ProductDTO> toDTOs(Collection<Product> products) {
        return products.stream().map(ws.ProductController::toDTO).collect(Collectors.toList());
    }

    @GET // means: to call this endpoint, we need to use the HTTP GET method
    @Path("/") // means: the relative url path is “/api/students/”
    public Response all() {
        String msg;
        try {
            GenericEntity<List<ProductDTO>> entity = new GenericEntity<List<ProductDTO>>(toDTOs(productBean.all())) {
            };
            return Response.status(Response.Status.OK)
                    .entity(entity)
                    .build();
        } catch (Exception e) {
            msg = "ERROR_GET_PRODUCTS --->" + e.getMessage();
        }
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(msg)
                .build();
    }

    @GET
    @Path("/name/{tosearch}")
    public Response getAdministratorBySearch(@PathParam("tosearch") String toSearch){
        Principal principal = securityContext.getUserPrincipal();
        if(securityContext.isUserInRole("Administrator")){
            GenericEntity<List<ProductDTO>> entity = new GenericEntity<List<ProductDTO>>(toDTOs(productBean.findBySearch(toSearch))) {
            };
            return Response.status(Response.Status.OK).entity(entity).build();
        }
        return Response.status(Response.Status.FORBIDDEN).build();
    }


    @GET
    @Path("/product/{id}")
    public Response getPartnerDetails(@PathParam("id") int id) {
        Principal principal = securityContext.getUserPrincipal();
        if (securityContext.isUserInRole("Administrator")) {
            Product product = productBean.find(id);
            if (product == null)
                return Response.status(Response.Status.NOT_FOUND).build();
            return Response.status(Response.Status.OK).entity(toDTO(product)).build();
        }
        return Response.status(Response.Status.FORBIDDEN).build();
    }


    @POST
    @Path("/")
    public Response createNewProduct(ProductDTO productDTO) throws MyEntityAlreadyExistsException, MyEntityNotFoundException, MyConstraintViolationException {
        Principal principal = securityContext.getUserPrincipal();
        if(securityContext.isUserInRole("Administrator")){
            Product product = productBean.create(
                    productDTO.getTypeCode(),
                    productDTO.getDescription(),
                    productDTO.getValue()
            );
            return Response.status(Response.Status.CREATED)
                    .entity(toDTO(product))
                    .build();
        }
        return Response.status(Response.Status.FORBIDDEN).build();
    }

    @PUT
    @Path("/{id}")
    public Response updateProduct(@PathParam("id") int id, ProductDTO productDTO){
        String msg;
        try {
            productBean.update(id, productDTO.getTypeCode(), productDTO.getDescription(), productDTO.getValue());
            return Response.status(Response.Status.OK).build();
        } catch (Exception e){
            msg = "ERROR_UPDATING_PRODUCT ---> " + e.getMessage();
        }
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(msg)
                .build();
    }

    @DELETE
    @Path("/{id}")
    public Response deletePartner(@PathParam("id") int id) throws MyEntityAlreadyExistsException, MyEntityNotFoundException, MyConstraintViolationException {
        String msg;
        try {
            productBean.remove(id);
            return Response.status(Response.Status.OK).build();
        } catch (Exception e) {
            msg = "ERROR_DELETING_PRODUCT --->" + e.getMessage();
        }
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(msg)
                .build();
    }
}
