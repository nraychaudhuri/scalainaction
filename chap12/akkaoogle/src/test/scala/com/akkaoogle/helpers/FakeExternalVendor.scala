package com.akkaoogle.helpers

import org.eclipse.jetty.server.{Request, Handler, Server}
import org.eclipse.jetty.server.handler.AbstractHandler
import javax.servlet.http.{HttpServletResponse, HttpServletRequest}

trait FakeExternalVendor {
  val server = new Server(8080)

  private def fakeExternalVendorHandler(price: String => Double): Handler = {
     new AbstractHandler {
       def handle(target: String, baseRequest: Request, request: HttpServletRequest, response: HttpServletResponse) = {
         response.setContentType("text/html;charset=utf-8");
         response.setStatus(HttpServletResponse.SC_OK);
         baseRequest.setHandled(true);
         response.getWriter().println(price(target));
       }
     }
   }

   def addFakeExternalResponse(price: String => Double): Unit = {
     server.setHandler(fakeExternalVendorHandler(price))
     server.start()
   }

}