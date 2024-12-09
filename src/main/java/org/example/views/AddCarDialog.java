//package org.example.views;
//
//import com.googlecode.lanterna.TerminalPosition;
//import com.googlecode.lanterna.TerminalSize;
//import com.googlecode.lanterna.gui2.*;
//import com.googlecode.lanterna.gui2.dialogs.MessageDialog;
//import org.example.controllers.CarController;
//import org.example.models.Car;
//
//import java.util.Arrays;
//
//public class AddCarDialog {
//    private static final CarController carController = new CarController();
//
//    public static void showAddCarDialog(WindowBasedTextGUI textGUI) {
//        TerminalSize screenSize = textGUI.getScreen().getTerminalSize();
//
//        Panel addCarPanel = new Panel();
//        addCarPanel.setLayoutManager(new GridLayout(2));
//
//        TextBox brandBox = new TextBox();
//        TextBox modelBox = new TextBox();
//        TextBox yearBox = new TextBox();
//        TextBox powerBox = new TextBox();
//        TextBox engineCapacityBox = new TextBox();
//        TextBox fuelTypeBox = new TextBox();
//        TextBox transmissionBox = new TextBox();
//        TextBox seatsBox = new TextBox();
//        TextBox pricePerDayBox = new TextBox();
//
//        addCarPanel.addComponent(new Label("Marka:"));
//        addCarPanel.addComponent(brandBox);
//        addCarPanel.addComponent(new Label("Model:"));
//        addCarPanel.addComponent(modelBox);
//        addCarPanel.addComponent(new Label("Rok:"));
//        addCarPanel.addComponent(yearBox);
//        addCarPanel.addComponent(new Label("Moc:"));
//        addCarPanel.addComponent(powerBox);
//        addCarPanel.addComponent(new Label("Pojemność silnika:"));
//        addCarPanel.addComponent(engineCapacityBox);
//        addCarPanel.addComponent(new Label("Rodzaj paliwa:"));
//        addCarPanel.addComponent(fuelTypeBox);
//        addCarPanel.addComponent(new Label("Skrzynia biegów:"));
//        addCarPanel.addComponent(transmissionBox);
//        addCarPanel.addComponent(new Label("Ilość miejsc:"));
//        addCarPanel.addComponent(seatsBox);
//        addCarPanel.addComponent(new Label("Cena za dzień:"));
//        addCarPanel.addComponent(pricePerDayBox);
//
//        Button addButton = new Button(" Dodaj samochód ", () -> {
//            // Walidacja danych
//            if (brandBox.getText().isEmpty() || modelBox.getText().isEmpty()) {
//                MessageDialog.showMessageDialog(textGUI, "Błąd", "Marka i model są wymagane.");
//                return;
//            }
//            // Tworzenie obiektu Car i dodawanie do bazy
//            Car car = new Car(
//                    0,
//                    brandBox.getText(),
//                    modelBox.getText(),
//                    Integer.parseInt(yearBox.getText()),
//                    Integer.parseInt(powerBox.getText()),
//                    Double.parseDouble(engineCapacityBox.getText()),
//                    fuelTypeBox.getText(),
//                    transmissionBox.getText(),
//                    Integer.parseInt(seatsBox.getText()),
//                    Double.parseDouble(pricePerDayBox.getText())
//            );
//            if (carController.addCar(car)) {
//                MessageDialog.showMessageDialog(textGUI, "Sukces", "Samochód został dodany.");
//                textGUI.getActiveWindow().close();
//            } else {
//                MessageDialog.showMessageDialog(textGUI, "Błąd", "Nie udało się dodać samochodu.");
//            }
//        });
//
//        Button cancelButton = new Button(" Anuluj ", () -> textGUI.getActiveWindow().close());
//        addCarPanel.addComponent(addButton);
//        addCarPanel.addComponent(cancelButton);
//
//        BasicWindow addCarWindow = new BasicWindow("Dodaj Samochód");
//        addCarWindow.setSize(new TerminalSize(screenSize.getColumns(), screenSize.getRows()));
//        addCarWindow.setPosition(new TerminalPosition(screenSize.getColumns() / 4 + 7, 0));
//        addCarWindow.setHints(Arrays.asList(Window.Hint.NO_DECORATIONS, Window.Hint.FIXED_POSITION, Window.Hint.FIXED_SIZE));
//        addCarWindow.setCloseWindowWithEscape(true);
//        addCarWindow.setComponent(addCarPanel.withBorder(Borders.singleLine()));
//        textGUI.addWindowAndWait(addCarWindow);
//    }
//}