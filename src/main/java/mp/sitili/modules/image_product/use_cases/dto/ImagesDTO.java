package mp.sitili.modules.image_product.use_cases.dto;

import java.util.List;

public interface ImagesDTO{
    String getProducto();
    Double getPrecio();
    Integer getCantidad();
    String getComentarios();
    Double getCalifiaccion();
    Boolean getEstado();
    String getCategoria();
    String getVendedor();
    String getNombreVendedor();
    String getApellidoVendedor();
    List<String> getImagenes();
}
