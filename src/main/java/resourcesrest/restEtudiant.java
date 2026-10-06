package resourcesrest;

import entities.Etudiant;
import entities.EtudiantList;
import entities.Option;
import metiers.EtudiantBusiness;
import metiers.OptionBusiness;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

@Path("etudiants")

public class restEtudiant {


    public static EtudiantBusiness optE = new EtudiantBusiness();
    public static OptionBusiness optB = new OptionBusiness();

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addEtudiant(Etudiant e) {
        if (optE.addEtudiant(e)) {
            return Response.status(200).entity(e).build();
        }
        return Response.status(404).entity(e).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllEtudiants() {

        List<Etudiant> etudiants = optE.getAllEtudiants();

        if (etudiants.isEmpty()) {
            return Response.status(Response.Status.NO_CONTENT).build();
        }

        return Response.status(200).entity(etudiants).build();
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getEtudiantById(@PathParam("id") String id) {

        Etudiant etudiant = optE.getEtudiantByIdentifiant(id);

        if (etudiant == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        return Response.status(200).entity(etudiant).build();
    }

    @DELETE
    @Path("/{id}")
    public Response deleteEtudiant(@PathParam("id") String id) {
        if (optE.deleteEtudiant(id)) {
            return Response.status(204).build();
        } else
            return Response.status(404).build();
    }

    @PUT
    @Path("{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateEtudiant(@PathParam("id") String id, Etudiant e) {

        e.setIdentifiant(id);
        if (optE.updateEtudiant(id, e)) {
            return Response.status(204).build();
        } else {
            return Response.status(404).build();
        }
    }


    @GET
    @Path("/option")
    @Produces(MediaType.APPLICATION_XML)
    public Response getEtudiantsByOption(@QueryParam("codeOption") int codeOption) {

        Option option = optB.getOptionByCode(codeOption);

        if (option == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        List<Etudiant> etudiants = new ArrayList<>();

        for (Etudiant e : optE.getEtudiantsByOption(option)) {

            etudiants.add(new Etudiant(
                    e.getIdentifiant(),
                    e.getNom(),
                    e.getPrenom(),
                    option,
                    e.getAnneeEtude(),
                    e.getEmail()
            ));
        }

        return Response.status(200)
                .entity(new EtudiantList(etudiants))
                .build();
    }
}