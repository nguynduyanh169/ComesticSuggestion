<%-- 
    Document   : detailproduct
    Created on : Mar 24, 2020, 9:30:38 AM
    Author     : anhnd
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="x" uri="http://java.sun.com/jsp/jstl/xml" %>
<!DOCTYPE html>

<!DOCTYPE html>
<html lang="en">
    <head>
        <title>Product Details</title>
        <meta charset="utf-8">
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <meta name="description" content="Little Closet template">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css" integrity="sha384-ggOyR0iXCbMQv3Xipma34MD+dH/1fQ784/j6cY/iJTQUOhcWr7x9JvoRxT2MZw1T" crossorigin="anonymous">
        <link href="plugins/font-awesome-4.7.0/css/font-awesome.min.css" rel="stylesheet" type="text/css">
        <link rel="stylesheet" type="text/css" href="plugins/flexslider/flexslider.css">
        <link rel="stylesheet" type="text/css" href="css/product.css">
        <link rel="stylesheet" type="text/css" href="css/product_responsive.css">
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
                        <ul class="d-flex flex-row align-items-start justify-content-start">
                            <li><a href="#">Women</a></li>
                            <li><a href="#">Men</a></li>
                            <li><a href="#">Kids</a></li>
                            <li><a href="#">Home Deco</a></li>
                            <li><a href="#">Contact</a></li>
                        </ul>
                    </nav>
                    <div class="header_right d-flex flex-row align-items-center justify-content-start ml-auto">
                        <!-- Search -->
                        <div class="header_search">
                            <form action="#" id="header_search_form">
                                <input type="text" class="search_input" placeholder="Search Item" required="required">
                                <button class="header_search_button"><img src="images/search.png" alt=""></button>
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
                            <div class="home_title">Product Details</div>
                        </div>
                    </div>
                </div>

                <!-- Product -->

                <c:set var="detaildoc" value="${sessionScope.DETAILPRODUCT}"/>
                <x:set var="details" select="$detaildoc//product" />
                <x:forEach var="element" select="$details" varStatus="counter">
                <div class="product">
                    <div class="container">
                        <div class="row">
                            <!-- Product Image -->
                            <div class="col-lg-6">
                                <div class="product_image_slider_container">
                                    <div id="slider" class="flexslider">
                                        <ul class="slides">
                                            <li>
                                                <img src="<x:out select="$element/imageLink"/>" />
                                            </li>
                                        </ul>
                                    </div>
                                </div>
                            </div>
                            <!-- Product Info -->
                            <div class="col-lg-6 product_col">
                                <div class="product_info">
                                    <div class="product_name"><x:out select="$element/productName"/></div>
                                    <div class="product_category">Xuất xứ: <x:out select="$element/origin"/>, <x:out select="$element/brand"/></div>
                                    <div class="product_category">Số lượng: <x:out select='$element/volume'/></span></div>
                                    <div class="product_category">Giá: <x:out select='$element/price'/></span></div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                </x:forEach>
            </div>

        </div>

        <script src="js/jquery-3.2.1.min.js"></script>
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
        <script src="plugins/flexslider/jquery.flexslider-min.js"></script>
        <script src="js/product.js"></script>
    </body>
</html>