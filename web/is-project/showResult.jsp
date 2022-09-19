<%--
    Document   : index
    Created on : 19.11.2016, 01:53:52
    Author     : gupta
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
  <style>
    .center {
      position: absolute;
      top: 40%;
      left: 50%;
      transform: translate(-50%, -50%);
      font-size: 18px;
    }
    .input {
      width: 1300px;
      height: 30px;
      top:60%;
    }
    p {
      display: inline;
    }
    .myform
    {
      top:60%
    }

  </style>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
  <title>project07 Web Search Engine</title>
</head>
<body>
<img class="center" src="../Images/search.jpg" alt="" width="600" height="300" />
<p>
<form class="myform"  action="Servlet.Display" method="get">
    <b>INPUT HERE  </b><input type="text" name="search" class="input" />
  <input type="submit" value="Search" />
</form>
</p>
</body>
</html>
