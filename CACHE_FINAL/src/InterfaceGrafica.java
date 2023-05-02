

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class InterfaceGrafica extends Application {

    private ConfigCache configCache = null;
    private Mapeamentos mapeamento;
    private String op = "aleatorio";
    private TextArea txtResult;
    private BorderPane root;

    private File acaoFileChooser(Stage primaryStage){
        FileChooser fc = new FileChooser();
        fc.setTitle("Titulo");
        return fc.showOpenDialog(primaryStage);       
    }

    private void criaOpcoesAlgoritmoSubstituicao(List<Button> addedBtns, TiposMapeamento tipoMapeamento){
         // Cria o grupo de botões de toggle
         ToggleGroup group = new ToggleGroup();
        
        // Cria os botões de toggle e adiciona-os ao grupo e ao contêiner HBox
        HBox toggleContainer = new HBox();
        toggleContainer.setAlignment(Pos.CENTER);
        toggleContainer.setSpacing(10);

        for (MetodosSubstituicao metodo : MetodosSubstituicao.getMetodos()) {
            RadioButton btn = new RadioButton(metodo.getDescricao());
            btn.setUserData(metodo);
            btn.setToggleGroup(group);
            toggleContainer.getChildren().add(btn);
        }

        group.selectedToggleProperty().addListener((obs, oldToggle, newToggle) -> {
            mapeamento.setTipoMapeamento(tipoMapeamento);
            RadioButton radio = (RadioButton)newToggle;
            MetodosSubstituicao metodoSub = (MetodosSubstituicao)radio.getUserData();

            mapeamento.executarAssociacao(metodoSub);
            txtResult.setText(mapeamento.getInfo());
        });

        // Adiciona o contêiner HBox à interface
        VBox vbox2 = new VBox(10);
        for (Button button : addedBtns) {
            vbox2.getChildren().addAll(button);
        }
        vbox2.getChildren().add(toggleContainer);
        vbox2.getChildren().add(txtResult);

        vbox2.setAlignment(Pos.CENTER);
        root.setCenter(vbox2);
    }

    private void criarBotoesOpcoes(){

        VBox vbox = new VBox(10);
        vbox.setAlignment(Pos.CENTER);
        List<Button> addedBtns = new ArrayList<Button>();

        for (TiposMapeamento tipo : TiposMapeamento.getTiposMapeamento()) {
            Button btn = new Button(tipo.getDescricao());
            btn.setUserData(tipo);            
            btn.setOnAction(e -> {
                Button source = (Button)e.getSource();      
                TiposMapeamento tipoMap = (TiposMapeamento)source.getUserData();
                switch (tipoMap) {
                    case DIRETO:
                        mapeamento.executar();
                        break;                
                    default:
                        criaOpcoesAlgoritmoSubstituicao(addedBtns, tipo);
                        break;
                }
                mapeamento.executar();
                txtResult.setText(mapeamento.getInfo());
            });
            addedBtns.add(btn);
            vbox.getChildren().add(btn);
        }
        
        root.setCenter(vbox);
    }

    private void createTxtResult(){
        // criação da área de texto
        txtResult = new TextArea();
        txtResult.setEditable(false);
        txtResult.setPrefHeight(100);
        txtResult.setPrefWidth(100);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        createTxtResult();
         
        VBox layout = new VBox(20);
        layout.setAlignment(Pos.CENTER);

        root = new BorderPane();       

        Button btn = new Button("Selecionar arquivo.");
        btn.setOnAction(e -> {
            File file = acaoFileChooser(primaryStage);
            
            if(file != null){
                txtResult.setText("Processando arquivo: " + file.getAbsolutePath());
                configCache = new ConfigCache(file.getAbsolutePath());
                mapeamento = new Mapeamentos(configCache);
                criarBotoesOpcoes();
            } else {
                txtResult.setText("Arquivo não selecionado.");
            }
        });    

        VBox vbox = new VBox(10, btn);
        vbox.setAlignment(Pos.CENTER);
        root.setCenter(vbox);        

        Scene scene = new Scene(root, 500, 400);

        primaryStage.setTitle("Simulador de Cache");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
