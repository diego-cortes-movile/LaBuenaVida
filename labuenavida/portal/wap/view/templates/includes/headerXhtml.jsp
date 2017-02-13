<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE HTML>

<html class=" js " lang="es">

<head>
<%@ taglib uri="/WEB-INF/portal.tld" prefix="p"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>

<c:set var="wap" value="${p:tree('wap')}" />
<c:set var="properties" value="${wap.properties}" />

<!-- Metas -->
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, maximum-scale=1">
<meta name="author" content="Movile">
<meta name="description" content="La Buena Vida | Claro">
<meta name="keywords" content="La Buena Vida | Claro">
<meta name="viewport" content="width=device-width, maximum-scale=1">
<meta property="og:title" content="La Buena Vida |  Claro" />
<meta property="og:description" content="Disfruta de tips para mejorar tu vida" />
<meta property="og:image" content="http://futbol.it.movile.com/bv/img/buenavida-fb.jpg" />

<title>La Buena Vida | Claro</title>

<link rel="shortcut icon" href="${p:absoluteURL(properties.smart_img_root)}/favicon.ico" type="image/x-icon">
<link href="${p:absoluteURL(properties.smart_css_root)}/bootstrap.min.css" rel="stylesheet" type="text/css">
<link href="${p:absoluteURL(properties.smart_css_root)}/heroic-features.css" rel="stylesheet" type="text/css">

<!-- HTML5 Shim and Respond.js IE8 support of HTML5 elements and media queries -->
<!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
<!--[if lt IE 9]>
        <script src="https://oss.maxcdn.com/libs/html5shiv/3.7.0/html5shiv.js"></script>
        <script src="https://oss.maxcdn.com/libs/respond.js/1.4.2/respond.min.js"></script>
    <![endif]-->

</head>
<body>
	<!-- LOGO -->
	<div class="logo">
		<img src="${p:absoluteURL(properties.smart_img_root)}/LogoBuenavida.png" alt="" />
	</div>