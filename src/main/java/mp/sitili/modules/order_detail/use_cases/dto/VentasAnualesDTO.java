package mp.sitili.modules.order_detail.use_cases.dto;

public interface VentasAnualesDTO {
    Integer getDay();
    Integer getVendidos_Dia();
    Double getTotal_Dia();
    Integer getWeek();
    Integer getVendidos_Semana();
    Double getTotal_Semana();
    Integer getMonth();
    Integer getVendidos_Mes();
    Double getTotal_Mes();
    Integer getAnio();
    Integer getVendidos_Anio();
    Double getTotal_Anio();
}
