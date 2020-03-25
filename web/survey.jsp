<%-- 
    Document   : survey
    Created on : Mar 22, 2020, 3:32:06 PM
    Author     : anhnd
--%>
<%@taglib prefix="x" uri="http://java.sun.com/jsp/jstl/xml" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@page contentType="text/html" pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html lang="en">

<head>
    <!-- Required meta tags-->
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <meta name="description" content="Colorlib Templates">
    <meta name="author" content="Colorlib">
    <meta name="keywords" content="Colorlib Templates">

    <!-- Title Page-->
    <title>Please take a survey</title>

    <!-- Icons font CSS-->
    <link href="vendor/mdi-font/css/material-design-iconic-font.min.css" rel="stylesheet" media="all">
    <link href="vendor/font-awesome-4.7/css/font-awesome.min.css" rel="stylesheet" media="all">
    <!-- Font special for pages-->
    <link href="https://fonts.googleapis.com/css?family=Open+Sans:300,300i,400,400i,600,600i,700,700i,800,800i" rel="stylesheet">

    <!-- Vendor CSS-->
    <link href="vendor/select2/select2.min.css" rel="stylesheet" media="all">
    <link href="vendor/datepicker/daterangepicker.css" rel="stylesheet" media="all">

    <!-- Main CSS-->
    <link href="css/main.css" rel="stylesheet" media="all">
</head>

<body>
    <div class="page-wrapper bg-gra-03 p-t-45 p-b-50">
        <div class="wrapper wrapper--w790">
            <div class="card card-5">
                <div class="card-heading">
                    <h2 class="title">Please take a survey</h2>
                </div>
                <div class="card-body">
                    <c:set var="categorydoc" value="${sessionScope.CATEGORY}"/>
                    <x:set var="listCategory" select="$categorydoc//category" />
                    <c:set var="branddoc" value="${sessionScope.BRAND}"/>
                    <x:set var="listBrand" select="$branddoc//brand" />
                    <c:set var="origindoc" value="${sessionScope.ORIGIN}"/>
                    <x:set var="listOrigin" select="$origindoc//origin" />
                    <form action="SurveyServlet" method="POST">
                        <div class="form-row">
                            <input type="hidden" name="userid" value="${sessionScope.USERID}" />
                            <div class="name">Which kind of cosmetic that you prefer to use ?</div><br/>
                            <div class="value">
                                <div class="input-group">
                                    <div class="rs-select2 js-select-simple select--no-search">
                                        <select name="categorySurvey">
                                            <option disabled="disabled" selected="selected">Choose option</option>
                                            <x:forEach var="category" select="$listCategory" varStatus="counter">
                                                <option value="<x:out select="$category/categoryId"/>"><x:out select="$category/categoryName"/></option>
                                            </x:forEach>
                                        </select>
                                        <div class="select-dropdown"></div>
                                    </div>
                                </div>
                            </div>
                            <div class="name">Which brand that you prefer to use ?</div><br/>
                            <div class="value">
                                <div class="input-group">
                                    <div class="rs-select2 js-select-simple select--no-search">
                                        <select name="brandSurvey">
                                            <option disabled="disabled" selected="selected">Choose option</option>
                                            <x:forEach var="brand" select="$listBrand" varStatus="counter">
                                                <option value="<x:out select="$brand/brand"/>"><x:out select="$brand/brand"/></option>
                                            </x:forEach>
                                        </select>
                                        <div class="select-dropdown"></div>
                                    </div>
                                </div>
                            </div>
                            <div class="name">Which origin that you prefer to use ?</div><br/>
                            <div class="value">
                                <div class="input-group">
                                    <div class="rs-select2 js-select-simple select--no-search">
                                        <select name="originSurvey">
                                            <option disabled="disabled" selected="selected">Choose option</option>
                                            <x:forEach var="origin" select="$listOrigin" varStatus="counter">
                                                <option value="<x:out select="$origin/origin"/>"><x:out select="$origin/origin"/></option>
                                            </x:forEach>
                                        </select>
                                        <div class="select-dropdown"></div>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div>
                            <button class="btn btn--radius-2 btn--red" type="submit">Submit</button>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div>

    <!-- Jquery JS-->
    <script src="vendor/jquery/jquery.min.js"></script>
    <!-- Vendor JS-->
    <script src="vendor/select2/select2.min.js"></script>
    <script src="vendor/datepicker/moment.min.js"></script>
    <script src="vendor/datepicker/daterangepicker.js"></script>

    <!-- Main JS-->
    <script src="js/global.js"></script>

</body><!-- This templates was made by Colorlib (https://colorlib.com) -->

</html>
<!-- end document-->
