package mp.sitili.modules.product.use_cases.dto;

import java.util.List;

public interface ProductDTO {
    Integer getProduct_id();
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
