<%--
  Created by IntelliJ IDEA.
  User: gupta
  Date: 31.01.2017
  Time: 18:34
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <script type="text/javascript">
        function validateform(){
            var name=document.register.cust.value;
            var ngrams=document.register.ngrams.value;
            var url=document.register.ad_url.value;
            var desc=document.register.desc.value;
            var budget=document.register.budget.value;
            var cost=document.register.agree.value;
            var img_url=document.register.ad_img_url.value;

            if (name==null || name==""){
                alert("Name can't be blank");
                return false;
            }else if(ngrams.length>80){
                alert("So many keywords");
                return false;
            }else if(desc.split("\\s+").length>30){
                alert("Description is exceeding word limit.");
                return false;
            }else if(budget>20){
                alert("Budget is exceeding limits");
                return false;
            }else if(cost=="No"){
                alert("Cost is fixed by internal athorities. Contact the administrator");
                return false;
            }
        }
    </script>
    <style>
        .set-style {
            background-color: lightcyan;
            width: 1000px;
            height: 600px;
            border: 20px solid deepskyblue;
            padding: 20px;
            margin: 10px;
        }
    </style>
    <title>Register Ads</title>
</head>
<body>
    <div class="set-style">
    <form name="register" action="Servlet.AdSave" method="get" onsubmit="return validateform()">
        <h1>Fill Ad information</h1><br><br><br>
        <b>Customer Name: </b><input type="text" name="cust" id="cust"/><br><br>
        <b>Set of ad activation keywords (e.g. database course, tukl informatik): </b>
            <input type="text" width="500px" name="ngrams" id="ngrams" required/><br><br>
        <b>Ad URL: </b><input type="url" name="ad_url" id="ad_url" pattern="http?://.+" required/><br><br>
        <b>Ad Description: (not more than 30 words) </b><input type="text" name="desc" id="desc" required/><br><br>
        <b>Budget(not more than 20$): </b><input type="text" name="budget" id="budget" required/><br><br>
        <b>Cost per click is 0.2$. Do you agree?:</b>
            <label><input type="checkbox" name="agree" checked>Yes</label>
            <label><input type="checkbox" name="agree">No</label>
        <b>If you want to add image please provide URL: </b><input type="text" name="ad_img_url" id="ad_img_url" pattern="http?://.+"/><br><br>
        <input type="submit" id="submit_ad" value="Submit"/><br> <br>
        <input type="reset" value="Reset"><br>
        <p id="msz"></p>
    </form>
    </div>
</body>
</html>
