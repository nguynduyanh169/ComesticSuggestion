<%-- 
    Document   : home
    Created on : Mar 23, 2020, 7:40:39 PM
    Author     : anhnd
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="x" uri="http://java.sun.com/jsp/jstl/xml" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="en">
    <head>
        <title>Home Page</title>
        <meta charset="utf-8">
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <meta name="description" content="Little Closet template">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css" integrity="sha384-ggOyR0iXCbMQv3Xipma34MD+dH/1fQ784/j6cY/iJTQUOhcWr7x9JvoRxT2MZw1T" crossorigin="anonymous">
        <link href="plugins/font-awesome-4.7.0/css/font-awesome.min.css" rel="stylesheet" type="text/css">
        <link rel="stylesheet" type="text/css" href="plugins/OwlCarousel2-2.2.1/owl.carousel.css">
        <link rel="stylesheet" type="text/css" href="plugins/OwlCarousel2-2.2.1/owl.theme.default.css">
        <link rel="stylesheet" type="text/css" href="plugins/OwlCarousel2-2.2.1/animate.css">
        <link rel="stylesheet" type="text/css" href="css/category.css">
        <link rel="stylesheet" type="text/css" href="css/category_responsive.css">
    </head>
    <body>
        <!-- Menu -->
        <div class="super_container">
            <!-- Header -->
            <header class="header">
                <div class="header_overlay"></div>
                <div class="header_content d-flex flex-row align-items-center justify-content-start">
                    <div class="logo">
                        <a href="#">
                            <div class="d-flex flex-row align-items-center justify-content-start">
                                <div><img src="images/logo_1.png" alt=""></div>
                                <div>BaoBao Cosmetics</div>
                            </div>
                        </a>	
                    </div>
                    <div class="hamburger"></div>
                    <nav class="main_nav">
                        <c:set var="categorydoc" value="${sessionScope.CATEGORY}"/>
                        <x:set var="listCategory" select="$categorydoc//category" />
                        <ul class="d-flex flex-row align-items-start justify-content-start">
                            <li><a href="#">All</a></li>
                                <x:forEach var="category" select="$listCategory" varStatus="counter">
                                <li><a href="#"><x:out select="$category/categoryName"/></a></li>
                                </x:forEach>
                        </ul>
                    </nav>
                    <div class="header_right d-flex flex-row align-items-center justify-content-start ml-auto">
                        <!-- Search -->
                        <div class="header_search">
                            <form action="SearchServlet" method="GET" id="header_search_form">
                                <input type="text" name="searchVal" class="search_input" placeholder="Search by name" value="${sessionScope.SEARCHVALUE}">
                                <button class="header_search_button" type="submit"><img src="images/search.png" alt=""></button>
                            </form>
                        </div>
                    </div>
                </div>
            </header>
            <div class="super_container_inner">
                <div class="super_overlay"></div>
                <!-- Home -->
                <div class="home">
                    <div class="home_container d-flex flex-column align-items-center justify-content-end">
                        <div class="home_content text-center">
                            <div class="home_title">Recommend For You</div>
                        </div>
                    </div>
                </div>
                <!-- Products -->
                <div class="products">
                    <div class="container">
                    </div>
                    <div class="row products_row products_container grid">
                        <c:set var="recommenddoc" value="${sessionScope.RECOMMEND}"/>
                        <c:if test="${not empty recommenddoc}">
                        <c:import url="WEB-INF/productList.xsl" var="xsldoc"/>
                        <x:transform xml="${recommenddoc}" xslt="${xsldoc}"/>
                        </c:if>
                        <c:if test="${empty recommenddoc}">
                            No Record!
                        </c:if>

                    </div>
                    <div class="row page_nav_row">
                        <div class="col">
                            <div class="page_nav">
                                <ul class="d-flex flex-row align-items-start justify-content-center">

                                </ul>
                            </div>
                        </div>
                    </div>
                    <c:set var="currentPage" value="${sessionScope.CURRENTPOS}"/>
                    <c:url var="nextLink" value="PagingServlet">
                        <c:param name="action" value="next"/>
                        <c:param name="curPage" value="${currentPage}"/>
                        <c:param name="search" value="${sessionScope.SEARCHVALUE}"/>
                    </c:url>
                    <c:url var="preLink" value="PagingServlet">
                        <c:param name="action" value="pre"/>
                        <c:param name="curPage" value="${currentPage}"/>
                        <c:param name="search" value="${sessionScope.SEARCHVALUE}"/>
                    </c:url>
                    <a href="${preLink}" class="previous">&laquo; Previous</a>
                    <a href="${nextLink}" class="next">Next &raquo;</a>
                </div>
            </div>
        </div>

        <script src="js/jquery-3.2.1.min.js"></script>
        <script src="styles/bootstrap-4.1.2/popper.js"></script>
        <script src="https://code.jquery.com/jquery-3.3.1.slim.min.js" integrity="sha384-q8i/X+965DzO0rT7abK41JStQIAqVgRVzpbzo5smXKp4YfRvH+8abtTE1Pi6jizo" crossorigin="anonymous"></script>
        <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.7/umd/popper.min.js" integrity="sha384-UO2eT0CpHqdSJQ6hJty5KVphtPhzWj9WO1clHTMGa3JDZwrnQq4sF86dIHNDz0W1" crossorigin="anonymous"></script>
        <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/js/bootstrap.min.js" integrity="sha384-JjSmVgyd0p3pXB1rRibZUAYoIIy6OrQ6VrjIEaFf/nJGzIxFDsf4x0xIM+B07jRM" crossorigin="anonymous"></script>
        <script src="plugins/greensock/TweenMax.min.js"></script>
        <script src="plugins/greensock/TimelineMax.min.js"></script>
        <script src="plugins/scrollmagic/ScrollMagic.min.js"></script>
        <script src="plugins/greensock/animation.gsap.min.js"></script>
        <script src="plugins/greensock/ScrollToPlugin.min.js"></script>
        <script src="plugins/OwlCarousel2-2.2.1/owl.carousel.js"></script>
        <script src="plugins/easing/easing.js"></script>
        <script src="plugins/progressbar/progressbar.min.js"></script>
        <script src="plugins/parallax-js-master/parallax.min.js"></script>
        <script src="plugins/Isotope/isotope.pkgd.min.js"></script>
        <script src="plugins/Isotope/fitcolumns.js"></script>
        <script src="js/category.js"></script>
    </body>
</html>
