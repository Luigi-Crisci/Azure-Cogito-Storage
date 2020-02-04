<%@page import="entity.*"%>
<%@page import="java.util.stream.Collectors"%>
<%@page import="java.util.stream.Collector"%>
<%@page import="java.math.*"%>
<%@page import="java.time.OffsetDateTime"%>
<%@page import="java.util.*"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page language="java" contentType="text/html; charset=ISO-8859-1"
   pageEncoding="ISO-8859-1"
   import="java.util.*,com.azure.storage.blob.models.*"%>
<!DOCTYPE html>
<html>
   <head>
    <% 
         String uriPath = request.getRequestURL().toString();
         String uriPathEffettiva = uriPath.substring(0, uriPath.lastIndexOf("WEB-INF")) ; 
         Account account = (Account) request.getSession().getAttribute("account");
         %>
      <script type="text/javascript" src="/webjars/jquery/2.2.4/jquery.min.js"></script>
      <!-- <script type="text/javascript" src="js/main.js"></script>  -->
      <script type="text/javascript" src="/js/main.js"></script>
      
      
      <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.1.3/css/bootstrap.min.css" integrity="sha384-MCw98/SFnGE8fJT3GXwEOngsV7Zt27NXFoaoApmYm81iuXoPkFOJwJ8ERdknLPMO" crossorigin="anonymous">
      <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.1.3/js/bootstrap.min.js" integrity="sha384-ChfqqxuZUCnJSK3+MXmPNIyE6ZbWh2IMqE241rYiqJxyMiZ6OW/JmZQ5stwEULTy" crossorigin="anonymous"></script>
      <meta charset="ISO-8859-1">
      <meta http-equiv="X-UA-Compatible" content="IE=edge">
      <meta name="viewport" content="width=device-width, initial-scale=1">
      <title>Account</title>
      <!-- Needed for making url position-resistant -->                    
     
      <link rel="stylesheet" type="text/css" href="/css/site.css">
   </head>
   <body>
      <div class="container-fluid">
         <div class="row">
            <div class="col-md-12">
               <h3>Ciao, <%=account.getNome()%></h3>
            </div>
         </div>
         <!-- FILE TABLE START -->
         <div class="row">
            <div class="col-md-8">
               <table class="table table-hover">
                  <thead class="thead-dark">
                     <tr>
                        <th>#</th>
                        <th>Name</th>
                        <th>Size(Kb)</th>
                        <th>Tag</th>
                        <th>Date</th>
                        <th></th>
                     </tr>
                  </thead>
                  <tbody>
                     <%
                        final List<BlobItemKeyStruct> blobs = (List<BlobItemKeyStruct>) session.getAttribute("Files");
                        int countTotal = 0;
                        int countDir = 0;
                        int countImg = 0;
                        int countOther = 0;
                        int idCounter = 0;
                        long sizeFile = 0;
                        String filename,dirName = "";
                        long allBlobSize = 0;
                        
                        int lastModifiedMount = 0;
                        int lastModifiedDay = 0;
                        int lastModifiedYear = 0;
                        int lastModifiedHour = 0;
                        int lastModifiedMinute = 0;
                        for (BlobItemKeyStruct b : blobs) {
                        	String key = b.getKey();
                        	if(!b.isDir()){
                        		sizeFile = b.getItem().getProperties().getContentLength();
                        		lastModifiedMount = b.getItem().getProperties().getLastModified().getMonthValue(); //mese
                        		lastModifiedDay = b.getItem().getProperties().getLastModified().getDayOfMonth(); //giorno
                        		lastModifiedYear = b.getItem().getProperties().getLastModified().getYear(); //anno
                        		lastModifiedHour = b.getItem().getProperties().getLastModified().getHour() + 1; //ora
                        		lastModifiedMinute = b.getItem().getProperties().getLastModified().getMinute(); //minuto
                        	}
                        	
                        	countTotal++;
                        	idCounter++;
                        	filename = b.getTrueName();
                        	
                        	//TODO: Only to make it work, a dir path should be added to BlobKeyItemStruct
                        	//It should be placed outside this FOR
                        	if(!b.getTrueName().equals("/"))
                        		if(b.isDir())
                        			dirName = b.getKey().substring(b.getKey().indexOf('=') + 1, b.getKey().lastIndexOf(filename));
                        		else if(b.getItem().getName().contains("/")) //Not in the root folder
                        			dirName = b.getItem().getName().substring(0, b.getItem().getName().lastIndexOf("/") + 1);
                        %>
                     <tr id="tableDataUser">
                        <td>
                           <%
                              if(filename.endsWith("/"))
                              {
                              	countDir++;
                              %> <img id="directoyImageFile" src="<%=uriPathEffettiva%>img/directoryImg.png" alt="dir" />
                           <%}
                              else if(filename.contains("png") || filename.contains("jpg") || filename.contains("jpeg"))
                              {
                              	countImg++;
                              %> <img id="fileImageFile" src="<%=uriPathEffettiva%>img/imageImg.png" alt="imm" />
                           <%} 
                              else
                              {
                              	countOther++;
                              %> <img id="fileImageFile" src="<%=uriPathEffettiva%>img/fileImg.png" alt="file" />
                           <%}%>
                        </td>
                        <td style="width: 30%" id="fileName">
                           <input class="form-control" type="text" id="rename_<%=idCounter%>" onkeyup="rename(event,'<%=dirName%>')" hidden/>
                           <a href="<%=key%>">
                           <label id="name_<%=idCounter%>"><%=b.getTrueName()%></label>
                           </a>
                        </td>
                        <td>
                           <%
                              if(!b.isDir())
                              {
                              	 float sizeMb = (float)sizeFile/(1024);
                              	 long roundedInt = Math.round(sizeMb * 100);
                              	 double result = (double) roundedInt/100;
                              	 allBlobSize += result;
                              %>
                           <label><%=result%></label>		
                           <%}%>
                        </td>
                        <%
                           try{	
                           	String tag = b.getItem().getMetadata().get("Tags").replaceAll("(?!\\s)\\W", "$0 ");
                           	if(tag!=null){ %>
                        <td style="width: 30%"><span class="ellipsis" id="indentTags"><%=tag%></span></td>
                        <% } 
                           }catch(Exception e){ %>
                        <td style="width: 30%"><span class="ellipsis" id="indentTags">No tags!</span></td>
                        <% }%>
                        <td>
                           <% 
                              if(!b.isDir()){
                              %>
                           <label><%=lastModifiedDay%>/<%=lastModifiedMount%>/<%=lastModifiedYear%> <%=lastModifiedHour%>:<%=lastModifiedMinute%></label>
                           <%}%>
                        </td>
                        <td style="width: 20%" id="testo" align="center">
                           <img id="deleteFile" src="<%=uriPathEffettiva%>img/trash.svg" alt="delete" onclick="functionDelete(this,'<%=dirName%>')"/>
                           <img  id="renameFile" src="<%=uriPathEffettiva%>img/renameFile.png" alt="rename" onclick="showRename(this)" />
                        </td>
                     </tr>
                     <%
                        }
                        %>
                  </tbody>
               </table>
               <!-- FILE TABLE END -->   
            </div>
            <div class="col-md-4" id="myborderDiv">
               <div>
                  <a href="/account">
                  <button type="submit" class="btn btn-success btn-lg btn-block">Home</button>
                  </a>
               </div>
               <div class="form-group">
                  <form role="form" method="post" action="/account/search">
                     <label for="InputSearch">Search:</label>
                     <input type="text" class="form-control" name="query"/>
                  </form>
               </div>
               <form role="form" method="post" id="createDirForm">
                  <label for="InputSearch">New directory name:</label>
                  <input type="text" name="dirName" class="form-control">
                  <!-- questo � il bottone --><input type="submit" class="form-control" value="Create" id="createDirSubmit" onClick="window.location.reload();" />
               </form>
               <form role="form" method="POST" enctype="multipart/form-data" id="fileUploadForm">
                  <label for="InputSearch">Choose files to upload:</label>
                  <input type="file" name="files" class="form-control">
                  <input type="submit" class="form-control" value="Upload" id="btnSubmit" />
               </form>
               <div class="form-group">
                  <label for="exampleFormControlTextarea1">Console</label>
                  <textarea class="form-control" id="textArea" rows="3"></textarea>
               </div>
               <div>
                  <p >
                     Total size of blob:
                     <%=allBlobSize%> (Kb)
                  </p>
                  <p>
                     All items:
                     <%=countTotal%>
                  </p>
                  <p>
                     Directory:
                     <%=countDir%>
                  </p>
                  <p>
                     Images:
                     <%=countImg%>
                  </p>
                  <p>
                     Files:
                     <%=countOther%>
                  </p>
               </div>
               <br>
               <div>
                  <a href="/logout">
                  <button type="submit" id="buttonLogOut" class="btn btn-danger btn-lg btn-block">Logout</button>
                  </a>
               </div>
               <label id="result"></label>
            </div>
         </div>
      </div>
      <div class="row">
         <div class="col-md-4">
            <img id="imageFooter" src="<%=uriPathEffettiva%>img/logo_standard.png" class="rounded-circle" />
         </div>
         <div class="col-md-4">
         </div>
         <div class="col-md-4" align="center">
            <dl>
               <dt>Professore:</dt>
               <dd>Vittorio Scarano</dd>
               <dt>Realizzato da:</dt>
               <dd>Luigi Crisci</dd>
               <dd>Giuseppe Di Palma</dd>
            </dl>
         </div>
      </div>
   </body>
</html>

