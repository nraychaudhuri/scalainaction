package com.akkaoogle.http.views

object index {
	
  def apply() = 
   <html>
	  <head>
	    <meta http-equiv="content-type" content="text/html; charset=ISO-8859-1"/>    
	    <title>
	        Akkaoogle: Find the cheapest deal on the web
	    </title>
	  </head>
	  <body> 
	    <center>
	        <img src="http://funnylogo.info/logo/Google/Red/Akkaoogle.aspx" style="border-width: 0px;"/>
	        <br/>
	        <br/>
	        <form action="/akkaoogle/search" method="GET">
	        <table cellpadding="0" cellspacing="0">
	          <tbody>
	            <tr valign="top">
	              <td align="center">
	    		        <input name="productDescription" id="productDescription" size="55" type="text"/>
	                <br/>
	                <br/>
	                <input name="sa" value="Find the cheapest deal" type="submit"/>
	              </td>
	           </tr>
	         </tbody>
	       </table>
	       </form>
	       <br/>
	       <br/>
	    </center>
	  </body>
	</html>
}

