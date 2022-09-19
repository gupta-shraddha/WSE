<%--
  User: gupta
  Date: 27.01.2017
  Time: 17:26
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <style>
    .set-parameter {
    background-color: lightcyan;
    width: 360px;
    height: 200px;
    border: 20px solid deepskyblue;
    padding: 20px;
    margin: 10px;
    }

    .input {
    width: 550px;
    height: 30px;
    top: 60%;
    }

    p {
    display: inline;
    }

    .myform {
    top: 60%
    }

    .simple {
    width: 25px;
    height: 30px;
    }

    </style>
    <title>Meta_Search_Engine_Group7</title>
</head>
<body>
<p>

<form class="myform" action="Servlet.MetaDisplay" method="get">
    <input type="button" onclick="window.location='configure.jsp'" value="Configure MetaSearchEngine"/>
    <CENTER>
        <b>INPUT HERE </b><input type="text" name="query" class="input"/>
        <input type="submit" value="Search"/><br> <br>
        <div class="set-parameter"><h3>Set-Parameters</h3><br><b> 1-TFIDF, 2-BM25, 3-BM25_Pagerank</b><br>
            <label><input type="radio" name="score" value="1" checked>1</label>
            <label><input type="radio" name="score" value="2">2</label>
            <label><input type="radio" name="score" value="3">3</label> <br>
            <b>Number of pages you want to retrieve ?</b><input type="text" name="k" class="simple"/><br>
        </div>
        <div>
            <img src="Images/search.jpg" alt="" width="600" height="300">
        </div>
    </CENTER>
</form>
</p>
</body>
</html>
