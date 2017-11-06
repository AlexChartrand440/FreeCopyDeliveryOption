# FreeCopyDeliveryOption

Initially, when we roll out delivery, the pickup points and the drop off points will be limited. This means, we will offer 
delivery only at certain stores and delivery to certain colleges. For instance, any user at Ahmedabad University will be able 
to get his prints delivered to is campus if he selects a store around him that offers that service. 
 
Your task is to create a stripped down version of this system using our current app as a reference. You can start building the 
app from the splash screen, then directly show a Google map like we do in the case of Remote Print, with some dummy stores (pins) 
that you have placed on the map. When a user clicks on the pin, the store page should open just like it does in the FreeCopy app, 
but with one addition. There should be an element on the page that allows the user to select if he wants the print to be delivered 
to the specific college. How you create this option is up to you. Use your creativity.  
 
We have made a dummy endpoint to which you will have to connect the frontend to. When a delivery job is sent, the endpoint 
should receive a specific command. 
 
Here are some things to keep in mind: 
 
• There are a total of 4 colleges. 
 
• There are a total of 3 stores. 
 
• One store can deliver to more than one college. 
 
• We emphasize on good, simple design. Refer to our app for guidelines. 
 
  
Technical  Details : 
 
Endpoint Location :  http://freecopy.in:8080/user/FCIntern/FCInternTask.aspx 
Parameters :  
1. JobID : Should be Integer/Long , Should be Unique 
2. PartnerID : Should be Integer/Long   
3. IsDelivery :  1 for Yes 0 for No 
4. Place : Should be Integer/Long 

Dummy Data : 
1. Use your Mobile Number as JobID (Increment it by 1 when needed) 
2. PartnerID : 101 – Store 1  
               102 – Store 2  
               103 – Store 3 
3. Place :  201 – College 1        
            202 – College 2        
            203 – College 3        
            204 – College 4   
                       
HTTP Request Format :  
POST /user/FCIntern/FCInternTask.aspx HTTP/1.1 
Accept: application/json 
Content-Type: application/x-www-form-urlencoded 
Content-Length: 43 
Host: 35.164.176.81 
Connection: close 
Accept-Encoding: gzip 
User-Agent: okhttp/3.2.0 
 
JobID=74&PartnerID=101&IsDelivery=1&Place=201 
 
 
HTTP Response :  
 
[{"status":"Success"}] 
