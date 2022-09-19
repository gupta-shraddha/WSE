<%@ page import="metaSearch.configure" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.ArrayList" %><%--
  Created by IntelliJ IDEA.
  User: gupta
  Date: 27.01.2017
  Time: 20:11
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <script src="http://code.jquery.com/jquery-2.1.0.min.js"></script>
    <script type="text/javascript">
        $(document).ready(function () {
            $('.update').click(function () {
                var sid = $(this).parent().parent().find('.sid').text().trim();
                var surl = $(this).parent().parent().find('.surl').val().trim();
                var Active = $(this).parent().parent().find('.sactive');
                var isActive;
                if($(Active).is(":checked")){
                    isActive = true;
                }
                else {
                    isActive = false;
                }
                alert(sid+','+surl+','+isActive);
                $('.update').val(sid+','+surl+','+isActive);
            });
            $('.delete').click(function () {
                var did =  $(this).parent().parent().find('.sid').text().trim();
                alert(did +' is deleted ');
                $('.delete').val(did);
                })
            $('#active').change(function () {
                if($(this).is(':checked')){
                    $(this).val('true');
                }else{
                    $(this).val('false');
                }
                alert($(this).val());
            })

        });
    </script>
    <style>
        .set-parameter {
            background-color: lightcyan;
            width: 1400px;
            height: 450px;
            border: 20px solid deepskyblue;
            padding: 20px;
            margin: 10px;
        }
    table {
        font-family: arial, sans-serif;
        border-collapse: collapse;
        width: 100%;
    }
    td, th {
        border: 1px solid #dddddd;
        text-align: left;
        padding: 8px;
    }
    tr:nth-child(even) {
        background-color: #dddddd;}
    </style>
</head>
<body>
<form action="Servlet.Config" method="get" name="cnfigform">
    <table>
        <thead><tr><th>Search Engine URL</th></tr></thead>
        <tfoot><tr>
            <td><button type="submit" name="add">Add </button></td>
        </tr></tfoot>
        <tbody>
        <tr>
            <td><input type="text" name="url"></td>
            <td><label><input type="checkbox" name="active" id="active" value="true" checked>Active</label></td>
            <td><input id="id" type="hidden" name="id"></td>
        </tr>
        </tbody>
    </table>
</form>
<div class="set-parameter">
    <h1>Added Search engines</h1>

    <table>
        <% configure conf=new configure();
            List<configure> items = new ArrayList<>();
            items=configure.getsavedEngine();
        for (int i=0;i < items.size();i++){%>
        <tbody>
        <tr>
            <td class="sid"> <%=items.get(i).id%></td>
            <td><input type="text" name="surl" class="surl" value=<%=items.get(i).url%>/></td>
            <%
                if(items.get(i).activated){
            %>
            <td><label><input type="checkbox" name="sactive" class="sactive" value="<%=items.get(i).activated%>;%>" checked>Active</label></td>
            <%
                }else{
            %>
            <td><label><input type="checkbox" name="sactive" class="sactive" value="<%=items.get(i).activated%>;%>">Active</label></td>
            <%
                }
            %>
            <form action="Servlet.ConfigUpdate" method="get">
            <td><button type="submit" name="update"  class="update" value="" >Update</button></td>
            </form>
            <form action="Servlet.ConfigDelete" method="get">
            <td><button type="SUBMIT" id="delete" name="delete" class="delete" value="">Delete</button></td>
            </form>
        </tr>
        </tbody>
        <% } %>
    </table>
</div>
</body>