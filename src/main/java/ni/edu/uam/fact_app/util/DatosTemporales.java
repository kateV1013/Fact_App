package ni.edu.uam.fact_app.util;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import ni.edu.uam.fact_app.model.Categoria;

public final class DatosTemporales {

    private static final ObservableList<Categoria> CATEGORIAS = FXCollections.observableArrayList(
            new Categoria(1, "Bebidas", true),
            new Categoria(2, "Alimentos", true),
            new Categoria(3, "Limpieza", true),
            new Categoria(4, "Tecnologia", true)
    );

    private DatosTemporales() {
    }

    public static ObservableList<Categoria> getCategorias() {
        return CATEGORIAS;
    }

    public static int siguienteIdCategoria() {
        return CATEGORIAS.stream()
                .mapToInt(Categoria::getId)
                .max()
                .orElse(0) + 1;
    }
}
