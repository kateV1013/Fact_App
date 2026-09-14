package ni.edu.uam.fact_app.util;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import ni.edu.uam.fact_app.model.Cargo;
import ni.edu.uam.fact_app.model.Categoria;
import ni.edu.uam.fact_app.model.Producto;

import java.math.BigDecimal;

public final class DatosTemporales {

    private static final ObservableList<Categoria> CATEGORIAS = FXCollections.observableArrayList(
            new Categoria(1, "Bebidas", true),
            new Categoria(2, "Alimentos", true),
            new Categoria(3, "Limpieza", true),
            new Categoria(4, "Tecnologia", true)
    );
    private static final ObservableList<Cargo> CARGOS = FXCollections.observableArrayList();
    private static final ObservableList<Producto> PRODUCTOS = FXCollections.observableArrayList();

    private DatosTemporales() {
    }

    public static ObservableList<Categoria> getCategorias() {
        return CATEGORIAS;
    }

    public static ObservableList<Producto> getProductos() {
        return PRODUCTOS;
    }

    public static ObservableList<Cargo> getCargos() {
        return CARGOS;
    }

    public static int siguienteIdCategoria() {
        return CATEGORIAS.stream()
                .mapToInt(Categoria::getId)
                .max()
                .orElse(0) + 1;
    }

    public static int siguienteIdProducto() {
        return PRODUCTOS.stream()
                .mapToInt(Producto::getId)
                .max()
                .orElse(0) + 1;
    }

    public static int siguienteIdCargo() {
        return CARGOS.stream()
                .mapToInt(Cargo::getId)
                .max()
                .orElse(0) + 1;
    }

    public static long totalProductosActivos() {
        return PRODUCTOS.stream()
                .filter(Producto::isActivo)
                .count();
    }

    public static int totalUnidadesInventario() {
        return PRODUCTOS.stream()
                .mapToInt(Producto::getExistencia)
                .sum();
    }

    public static long totalCategoriasRegistradas() {
        return CATEGORIAS.size();
    }

    public static long totalCargosRegistrados() {
        return CARGOS.size();
    }

    public static BigDecimal valorTotalInventario() {
        return PRODUCTOS.stream()
                .map(producto -> producto.getPrecioVenta().multiply(BigDecimal.valueOf(producto.getExistencia())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
