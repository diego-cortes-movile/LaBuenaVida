<%@include file="../includes/taglibs.jsp"%>
<%@include file="../includes/headerXhtml.jsp"%>

<!-- Page Content -->
<div>
	<!-- Jumbotron Header -->
	<header class="jumbotron hero-spacer">
	
		<c:if test="${empty videos}">
			<div class="verde"></div>
			<h1> Pr&oacute;ximamente encontrar&aacute;s contenido sobre este tema.</h1>
		</c:if>
		
		<c:if test="${not empty videos}">
			<div class="verde"></div>
			<h1>${videoActual.name}</h1>
			<strong class="date">D&iactue;a 1</strong>
			<video id="player1" width="100%" height="" type="video/mp4" controls preload="none"
				src="${videoActual.downloadUrl}"
				poster="${videoActual.previewImg }">
			</video>
		</c:if>
		
		
	</header>

	<!-- Title -->
	<section class="container tagss">
		<h1>Otros temas que puedes ver</h1>
		<div class="tags">
			<a href="#" class="success">Salom&oacute;n</a>
			<a href="#" class="primary">Disfrutar</a>
			<a href="#" class="danger">Cuidarte</a>
			<a href="#" class="warning">Mejorar</a>
		</div>
	</section>

	<!-- Relacionados --->
	<section class="container relacionados">
		<h1>Videos Relacionados</h1>
		<c:if test="${empty videos}">
			<div class="verde"></div>
			<h1> Pr&oacute;ximamente encontrar&aacute;s contenido sobre este tema.</h1>
		</c:if>
		<c:if test="${not empty videos}">
			<div class="row">
				
				<c:forEach items="${videos}" var="vid">
					<div class="col-lg-4 col-md-4 col-xs-4">
	               <a href="#">
	                   <div class="thumbnail2">
	                    <img src="${vid.previewImg}" alt="">
	                    <div class="caption">${vid.name}</div>
	                </div>
	                 </a>
	            </div>
				</c:forEach>
				
	            
	            
            </div>
		</c:if>
		
		<!-- /.row -->
	</section>

	<%@include file="../includes/footerXhtml.jsp"%>