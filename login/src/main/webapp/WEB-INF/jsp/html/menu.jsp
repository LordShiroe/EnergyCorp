<% Cookie[] cookies = request.getCookies();
    String nombre = null;
    for (Cookie cookie : cookies) {
        if (cookie.getName().equals("USER")) {
            nombre = cookie.getValue();
        }
    }

%>
<div class="col-sm-12">
    <div class="navbar navbar-expand-lg navbar-dark primary-color">
        <div class="container-fluid">
            <div class="navbar-header">
                <button type="button" class="navbar-toggle" data-toggle="collapse" data-target=".navbar-responsive-collapse">
                    <span class="icon-bar"></span>
                    <span class="icon-bar"></span>
                    <span class="icon-bar"></span>
                </button>
                <a class="navbar-brand">
                    <img src="/estaticos/sistema/img/logo.png" alt="EnergyCorp" height="120%">
                </a>
            </div>
            <div class="navbar-collapse collapse navbar-responsive-collapse">
                <ul class="nav navbar-nav">
                    <li class="nav-item"><a class=" nav-link white-text" style="font-size: 1em;padding: 5px;">
                            <b>EnergyCorp</b><br>
                            <%=nombre%>
                        </a>
                    </li>
                </ul>

                <ul class="nav navbar-nav navbar-right">
                    <li class="dropdown">
                    <li><a class="white-text" href="#" onclick="cerrar_sesion()">Cerrar Sesion</a></li>
                    </li>
                </ul>
            </div>
        </div>
    </div>
</div>


