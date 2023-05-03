import java.io.File;
import javafx.application.Application;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class InterfaceGrafica extends Application {

    private GridPane grid;
    private TextArea txtResult;
    private String configTextFile;
    private String dataTextFile;
    private HBox toggleAlgoritmosBox;
    private MetodosSubstituicao selectedMetodoSubstituicao;
    private TiposMapeamento selectedTipoMapeamento;

    int resultColumns = 20;
    int resultLines = 20;

    @Override
    public void start(Stage primaryStage) throws Exception {
        criarLayoutTela(primaryStage);         
    }

    public static void main(String[] args) {
        launch(args);
    }

    private void criarLayoutTela(Stage primaryStage){
        
        grid = createGrid();
        txtResult = createTextArea();
        Text scenetitle = createText();
        Button btnConfig = createButtonSelecaoConfig(primaryStage);
        Button btnDados = createButtonSelecaoDados(primaryStage);
        HBox toggleMapeamentosBox = criarOpcoesMapeamentos();
        toggleAlgoritmosBox = criarOpcoesAlgoritmoSubstituicao();
        Button btnExecutar = createButtonExecutar();
        
        grid.add(btnConfig, 0, 0, 1, 1);
        grid.add(btnDados, 1, 0, 1, 1);
        grid.add(scenetitle, 0, 1, 3, 1);
        grid.add(toggleMapeamentosBox, 0, 2, resultColumns, 1);        
        grid.add(txtResult, 0, 4, resultColumns, resultLines);
        grid.add(btnExecutar, 0, grid.getRowCount() + 1, grid.getColumnCount(), 1);

        Scene scene = new Scene(grid, 500, 400);

        primaryStage.setTitle("Simulador de Cache");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private GridPane createGrid(){
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));
        return grid;
    }

    private Text createText(){
        Text scenetitle = new Text("Mapeamentos");
        scenetitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
        GridPane.setHalignment(scenetitle, HPos.CENTER);
        return scenetitle;
    }

    private TextArea createTextArea(){
        TextArea txtResult = new TextArea();
        txtResult.setEditable(false);
        txtResult.setPrefHeight(100);
        txtResult.setPrefWidth(100);
        return txtResult;
    }

    private Button createButtonSelecaoConfig(Stage primaryStage){
        Button btn = new Button("Selecionar Configuração");
        btn.setOnAction(e -> {
            configTextFile = acaoFileChooser(primaryStage);
        });
        return btn;
    }

    private Button createButtonSelecaoDados(Stage primaryStage){
        Button btn = new Button("Selecionar Dados");
        btn.setOnAction(e -> {
            dataTextFile = acaoFileChooser(primaryStage);
        });
        return btn;
    }

    private Button createButtonExecutar(){
        Button btn = new Button("Executar");
        btn.setOnAction(e -> {
            clearResultMessage();
            if(validateFiles()){
                exibirInformacoesSelecionadas();
                executar();
            }
        });
        return btn;
    }

    private void exibirInformacoesSelecionadas() {
        setResultMessage("Arquivo de configurações: " + configTextFile);
        setResultMessage("Arquivo de dados: " + dataTextFile);
        setResultMessage("Tipo de mapeamento: " + selectedTipoMapeamento.getDescricao());
        if (selectedTipoMapeamento != TiposMapeamento.DIRETO) {
            setResultMessage("Método de substituição: " + selectedMetodoSubstituicao.getDescricao());
        }
    }

    private void executar(){
        Mapeamentos mapeamento = new Mapeamentos(configTextFile, dataTextFile, selectedTipoMapeamento);
        
        switch (selectedTipoMapeamento) {
            case DIRETO:
                mapeamento.executar();
                setResultMessage(mapeamento.getInfo());
                break;                
            default:
                mapeamento.executar(selectedMetodoSubstituicao);
                break;
        }
        setResultMessage(mapeamento.getInfo());
    }

    private String acaoFileChooser(Stage primaryStage){
        FileChooser fc = new FileChooser();
        fc.setTitle("Titulo");
        File file = fc.showOpenDialog(primaryStage);
        return file == null ? "" : file.getAbsolutePath();
    } 
    
    private HBox criarOpcoesMapeamentos(){
        ToggleGroup groupMapeamento = new ToggleGroup();
        HBox toggleContainer = createContainer();
        boolean selected = true;

        for (TiposMapeamento tipoMapeamento : TiposMapeamento.getTiposMapeamento()) {
            RadioButton btn = new RadioButton(tipoMapeamento.getDescricao());
            btn.setUserData(tipoMapeamento);
            btn.setToggleGroup(groupMapeamento);
            if (selected) {
                btn.setSelected(true);
                selected = false;
                selectedTipoMapeamento = tipoMapeamento;
            }            
            toggleContainer.getChildren().add(btn);
        }

        groupMapeamento.selectedToggleProperty().addListener((obs, oldToggle, newToggle) -> {
            RadioButton radio = (RadioButton)newToggle;            
            selectedTipoMapeamento = (TiposMapeamento)radio.getUserData();

            switch (selectedTipoMapeamento) {
                case DIRETO:
                    trocarStatusGridAlgoritmos(true);
                    break;                
                default:
                    trocarStatusGridAlgoritmos(false);
                    break;
            }
        });

        return toggleContainer;
    }    

    private boolean validateFiles(){
        boolean canExecute = true;

        if (configTextFile == null || configTextFile.trim().isEmpty()) {
            setResultMessage("Arquivo de configurações não informado.");
            canExecute = false;
        }

        if (dataTextFile == null || dataTextFile.trim().isEmpty()) {
            setResultMessage("Arquivo de dados não informado.");
            canExecute = false;
        }

        return canExecute;
    }

    private HBox criarOpcoesAlgoritmoSubstituicao(){
        ToggleGroup groupAlgoritmos = new ToggleGroup();
       
        HBox toggleContainer = createContainer();

        boolean selected = true;
        for (MetodosSubstituicao metodo : MetodosSubstituicao.getMetodos()) {
            RadioButton btn = new RadioButton(metodo.getDescricao());
            btn.setUserData(metodo);
            btn.setToggleGroup(groupAlgoritmos);
            if (selected) {
                btn.setSelected(true);
                selectedMetodoSubstituicao = metodo;
                selected = false;
            }   
            toggleContainer.getChildren().add(btn);
        }

        groupAlgoritmos.selectedToggleProperty().addListener((obs, oldToggle, newToggle) -> {
            RadioButton radio = (RadioButton)newToggle;
            selectedMetodoSubstituicao = (MetodosSubstituicao)radio.getUserData();
        });

        return toggleContainer;
    }

    private void trocarStatusGridAlgoritmos(boolean hide){
        if (hide) {
            grid.getChildren().remove(toggleAlgoritmosBox);
        } else {
            if(!grid.getChildren().contains(toggleAlgoritmosBox)){
                grid.add(toggleAlgoritmosBox, 0, 3, resultColumns, 1);
            }            
        }        
    }

    private HBox createContainer(){
        HBox toggleContainer = new HBox();
        toggleContainer.setAlignment(Pos.CENTER);
        toggleContainer.setSpacing(10);
        return toggleContainer;
    }

    private void setResultMessage(String message){
        txtResult.appendText(message + "\n");
    }

    private void clearResultMessage(){
        txtResult.clear();
    }
}