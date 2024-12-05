package edu.unizg.foi.nwtis.asaric.vjezba_08_dz_3.nadzor;

import edu.unizg.foi.nwtis.asaric.vjezba_08_dz_3.podaci.Kazna;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.HeaderParam;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("nwtis/v1/api/nadzori/kazne")
@RequestScoped
public class KazneResurs {

  @Inject
  KazneSlanje kazneSlanje;

  @POST
  @Consumes({MediaType.APPLICATION_JSON})
  @Produces({MediaType.APPLICATION_JSON})
  public Response postJsonDodajKaznu(@HeaderParam("Accept") String tipOdgovora, Kazna novaKazna) {
	  System.out.println("KLIJENTI");
    var uspjehSlanja = kazneSlanje.novaKazna(novaKazna);
    if (uspjehSlanja) {
      return Response.status(Response.Status.OK).build();
    } else {
      return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
          .entity("Neuspješni upis kazne u bazu podataka.").build();
    }
  }

}
