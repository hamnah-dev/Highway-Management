package com.motorway.gui;

import com.motorway.enums.RoadBlockageLevel;
import com.motorway.enums.Severity;
import com.motorway.filehandler.IncidentFileHandler;
import com.motorway.filehandler.TeamFileHandler;
import com.motorway.manager.IncidentManager;
import com.motorway.model.*;
import com.motorway.service.AuthenticationService;
import com.motorway.service.MapService;
import com.motorway.service.WeatherService;
import javafx.animation.PauseTransition;
import javafx.animation.SequentialTransition;
import javafx.animation.TranslateTransition;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.scene.web.WebView;
import javafx.scene.web.WebEngine;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;
import netscape.javascript.JSObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import javafx.scene.paint.LinearGradient;
import javafx.scene.image.ImageView;

import static com.motorway.filehandler.IncidentFileHandler.loadIncidents;


public class HelloApplication extends Application {

    // -------------------------- GLOBALS --------------------------
    private Stage primaryStage;
    private AuthenticationService auth = AuthenticationService.getInstance();
    private IncidentManager incidentManager = new IncidentManager();
    private Pane root;
    private Scene scene;

    private double selectedLat = 0;
    private double selectedLng = 0;
    private String selectedAddress = "Unknown";

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        this.primaryStage = stage;
        primaryStage.setTitle("Highway Management System");
        Image ambulanceImg = new Image(getClass().getResource("/images/ambulance2.png").toString());
        showLoadingScreen(ambulanceImg, () -> this.showLoginScreen());
    }

    // -------------------------- LOADING SCREEN --------------------------
    private void showLoadingScreen(Image animImage, Runnable afterLoading) {
        root = new Pane();

        LinearGradient gradient = new LinearGradient(
                0, 0, 0, 1,
                true, javafx.scene.paint.CycleMethod.NO_CYCLE,
                new javafx.scene.paint.Stop(0, Color.web("#dff5e1")),
                new javafx.scene.paint.Stop(1, Color.web("#b8e6c1"))
        );
        root.setBackground(new Background(new BackgroundFill(gradient, CornerRadii.EMPTY, null)));

        scene = new Scene(root, 800, 500);
        primaryStage.setScene(scene);
        primaryStage.show();

        addBackgroundIcons();

        ImageView anim = new ImageView(animImage);
        anim.setFitWidth(120);
        anim.setPreserveRatio(true);
        anim.setLayoutX(-150);
        anim.setLayoutY(scene.getHeight() / 2 - 60);
        root.getChildren().add(anim);

        TranslateTransition moveIn = new TranslateTransition(Duration.seconds(1), anim);
        moveIn.setByX(scene.getWidth() / 2 + 50);

        PauseTransition pause = new PauseTransition(Duration.seconds(1));

        TranslateTransition moveOut = new TranslateTransition(Duration.seconds(1), anim);
        moveOut.setByX(scene.getWidth());

        SequentialTransition seq = new SequentialTransition(moveIn, pause, moveOut);
        seq.setOnFinished(e -> afterLoading.run());
        seq.play();
    }
    private void addBackgroundIcons() {
        Random rand = new Random();
        Image policeImg = new Image(getClass().getResource("/images/police2.png").toString());
        Image fireImg = new Image(getClass().getResource("/images/fire2.png").toString());
        Image ambulanceImg = new Image(getClass().getResource("/images/ambulance2.png").toString());

        Image[] icons = new Image[]{policeImg, fireImg, ambulanceImg};
        int totalIcons = 20;
        for (int i = 0; i < totalIcons; i++) {
            Image icon = icons[rand.nextInt(icons.length)];
            ImageView iv = new ImageView(icon);
            double size = 30 + rand.nextDouble() * 20;
            iv.setFitWidth(size);
            iv.setPreserveRatio(true);
            iv.setOpacity(0.05 + rand.nextDouble() * 0.07);
            iv.setLayoutX(rand.nextDouble() * (scene.getWidth() - size));
            iv.setLayoutY(rand.nextDouble() * (scene.getHeight() - size));
            root.getChildren().add(iv);
        }
    }

    // -------------------------- LOGIN SCREEN --------------------------
    private void showLoginScreen() {

        Pane root = new Pane();
        root.setStyle("-fx-background-color: linear-gradient(to bottom, #dff5e1, #b8e6c1 );");


        VBox loginBox = new VBox(20);
        loginBox.setPadding(new Insets(30));
        loginBox.setStyle("-fx-background-color: white; -fx-background-radius: 15;");
        loginBox.setAlignment(Pos.CENTER_LEFT);


        Label title = new Label("Highway Management System");
        title.setStyle("-fx-font-size: 24px; -fx-font-weight: bold");
        Label subtitle = new Label("Please sign in to continue");
        subtitle.setStyle(
                "-fx-font-size: 13px;" +
                        "-fx-text-fill: #6b8f7a;"

        );




        GridPane grid = new GridPane();
        grid.setHgap(12);
        grid.setVgap(14);
        grid.setAlignment(Pos.CENTER);

        Label userLabel = new Label("Username:");
        TextField usernameField = new TextField();
        GridPane.setConstraints(userLabel, 0, 0);
        GridPane.setConstraints(usernameField, 1, 0);

        Label passLabel = new Label("Password:");
        PasswordField passwordField = new PasswordField();
        GridPane.setConstraints(passLabel, 0, 1);
        GridPane.setConstraints(passwordField, 1, 1);

        Label errorLabel = new Label();
        errorLabel.setStyle("-fx-text-fill: red;");
        GridPane.setConstraints(errorLabel, 0, 2, 2, 1);

        grid.getChildren().addAll(userLabel, usernameField, passLabel, passwordField, errorLabel);


        HBox btnBox = new HBox(50);
        btnBox.setAlignment(Pos.CENTER);
        Button loginButton = new Button("Login");
        loginButton.setStyle("-fx-background-color: #dff5e1; -fx-font-weight: bold;");
        btnBox.getChildren().add(loginButton);


        loginBox.getChildren().addAll(title, subtitle, grid, btnBox);


        loginBox.setLayoutX(200);
        loginBox.setLayoutY(120);

        root.getChildren().add(loginBox);

        Scene scene = new Scene(root, 800, 500);
        primaryStage.setScene(scene);
        primaryStage.show();

        loginButton.setOnAction(e -> {
            String u = usernameField.getText().trim();
            String p = passwordField.getText().trim();
            if (u.isEmpty() || p.isEmpty()) {
                errorLabel.setText("Enter username and password");
                return;
            }

            User user = auth.login(u, p);
            if (user == null) {
                errorLabel.setText("Invalid credentials");
                return;
            }

            incidentManager.getAllIncidents().clear();
            List<Incident> loaded = loadIncidents();
            if (loaded != null) {
                incidentManager.getAllIncidents().addAll(loaded);
            }

            List<Team> savedTeams = TeamFileHandler.loadTeams();
            if (savedTeams != null) {
                savedTeams.forEach(incidentManager::addTeam);
            }

            if (user.isAdmin()) {
                Image adminCarImg = new Image(getClass().getResource("/images/truck2.png").toString());
                showLoadingScreen(adminCarImg, this::showAdminDashboard);
            } else {
                Image userCarImg = new Image(getClass().getResource("/images/usercar2.png").toString());
                showLoadingScreen(userCarImg, this::showUserDashboard);
            }

        });
    }


    // -------------------------- ADMIN DASHBOARD --------------------------
    private void showAdminDashboard() {
        BorderPane root = new BorderPane();
        root.setPadding(new Insets(12));
        root.setStyle("-fx-background-color: #b8e6c1;");

        HBox top = new HBox();
        top.setAlignment(Pos.CENTER);
        Label lbl = new Label("Admin Dashboard");
        lbl.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: white;");
        top.getChildren().add(lbl);
        root.setTop(top);

        VBox left = new VBox(12);
        left.setPadding(new Insets(10));
        left.setAlignment(Pos.TOP_LEFT);

        Region spacer = new Region();
        VBox.setVgrow(spacer, Priority.ALWAYS);

        Button manageIncBtn = new Button("Manage Incidents");
        Button manageTeamBtn = new Button("Manage Teams");
        Button reportBtn = new Button("Overall Report");
        Button logout = new Button("Logout");

        String btnStyle = "-fx-background-color: #dff5e1; -fx-font-weight: bold;";
        manageIncBtn.setStyle(btnStyle);
        manageTeamBtn.setStyle(btnStyle);
        reportBtn.setStyle(btnStyle);
        logout.setStyle(btnStyle);

        left.getChildren().addAll(spacer, manageIncBtn, manageTeamBtn, reportBtn, logout);
        root.setLeft(left);

        VBox center = new VBox(12);
        center.setPadding(new Insets(10));
        center.setStyle("-fx-background-color: #dff5e1; -fx-background-radius: 10;");
        center.setAlignment(Pos.TOP_CENTER);

        Label prioLabel = new Label("Priority Incidents:");
        prioLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
        prioLabel.setAlignment(Pos.CENTER);

        ListView<String> prioList = new ListView<>();
        prioList.setStyle("-fx-background-color: white; -fx-border-radius: 5; -fx-background-radius: 5;");
        loadAdminPriorityList(prioList);

        center.getChildren().addAll(prioLabel, prioList);
        root.setCenter(center);

        logout.setOnAction(e -> {
            auth.logout();
            showLoginScreen();
        });

        manageTeamBtn.setOnAction(e -> showManageTeams(null, prioList));
        manageIncBtn.setOnAction(e -> showManageIncidents(prioList, null));
        reportBtn.setOnAction(e -> showOverallReport());

        Scene sc = new Scene(root, 800, 500);
        primaryStage.setScene(sc);

    }

    // -------------------------- MANAGE TEAMS --------------------------
    private void showManageTeams(Label stats, ListView<String> prioList) {
        Stage st = new Stage();
        st.initModality(Modality.APPLICATION_MODAL);
        st.setTitle("Manage Teams");

        VBox root = new VBox(12);
        root.setPadding(new Insets(12));
        root.setStyle("-fx-background-color: #dff5e1;"); // lighter green background

        ListView<String> list = new ListView<>();
        list.setStyle("-fx-background-color: white; -fx-border-radius: 5; -fx-background-radius: 5;"); // keep list white
        refreshTeamList(list);

        Button addBtn = new Button("Add Team");
        addBtn.setStyle("-fx-background-color: #b8e6c1; -fx-font-weight: bold;"); // darker green button
        addBtn.setOnAction(e -> {
            TextInputDialog d = new TextInputDialog();
            d.setHeaderText("Enter team name:");
            Optional<String> r = d.showAndWait();
            r.ifPresent(n -> {
                Team t = new Team(incidentManager.getAllTeams().size() + 1, n, 5);
                incidentManager.addTeam(t);
                TeamFileHandler.saveTeams(incidentManager.getAllTeams());
                System.out.println("Saved teams: " + incidentManager.getAllTeams().size());
                refreshTeamList(list);
            });
        });

        root.getChildren().addAll(list, addBtn);
        st.setScene(new Scene(root, 400, 400));
        st.show();
    }


    // -------------------------- MANAGE INCIDENTS (ADMIN) --------------------------
    private void showManageIncidents(ListView<String> prioList, Label stats) {
        Stage st = new Stage();
        st.initModality(Modality.APPLICATION_MODAL);
        st.setTitle("Manage Incidents");

        VBox root = new VBox(12);
        root.setPadding(new Insets(12));
        root.setStyle("-fx-background-color: #dff5e1;");

        HBox controls = new HBox(12);
        Label fl = new Label("Filter:");
        ComboBox<String> filter = new ComboBox<>();
        filter.getItems().addAll("All", "REPORTED", "DISPATCHED", "RESOLVED");
        filter.setValue("All");
        filter.setStyle("-fx-background-color: #b8e6c1;");
        Button refresh = new Button("Apply Filter");
        Button assign = new Button("Assign Team");
        Button resolve = new Button("Mark Resolved");
        Button showRep = new Button("Show Report");


        String btnStyle = "-fx-background-color: #b8e6c1; -fx-font-weight: bold;";
        refresh.setStyle(btnStyle);
        assign.setStyle(btnStyle);
        resolve.setStyle(btnStyle);
        showRep.setStyle(btnStyle);

        controls.getChildren().addAll(fl, filter, refresh, assign, resolve, showRep);

        TableView<Incident> table = new TableView<>();
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        table.setStyle("-fx-background-color: white;");

        TableColumn<Incident, Integer> c1 = new TableColumn<>("ID");
        c1.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<Incident, String> c2 = new TableColumn<>("Type");
        c2.setCellValueFactory(d -> new javafx.beans.property.SimpleStringProperty(d.getValue().getType()));

        TableColumn<Incident, String> c3 = new TableColumn<>("Status");
        c3.setCellValueFactory(d -> new javafx.beans.property.SimpleStringProperty(d.getValue().getStatus().name()));

        TableColumn<Incident, String> c4 = new TableColumn<>("Location");
        c4.setCellValueFactory(d -> new javafx.beans.property.SimpleStringProperty(
                d.getValue().getLocation() == null ? "N/A" : d.getValue().getLocation().getAddress()
        ));

        table.getColumns().addAll(c1, c2, c3, c4);

        javafx.collections.ObservableList<Incident> tableData =
                FXCollections.observableArrayList(incidentManager.getAllIncidents());
        table.setItems(tableData);

        Runnable refreshTable = () -> {
            List<Incident> filtered = applyFilter(filter.getValue());
            tableData.setAll(filtered);
        };

        refreshTable.run();

        refresh.setOnAction(e -> refreshTable.run());

        assign.setOnAction(e -> {
            Incident selected = table.getSelectionModel().getSelectedItem();
            if (selected == null) {
                alert("Select an incident");
                return;
            }

            List<Team> avail = incidentManager.getAvailableTeams();
            if (avail.isEmpty()) {
                alert("No available teams");
                return;
            }

            ChoiceDialog<Team> dlg = new ChoiceDialog<>(avail.get(0), avail);
            dlg.setHeaderText("Assign Team to Incident ID " + selected.getId());
            Optional<Team> res = dlg.showAndWait();
            res.ifPresent(t -> {
                try {
                    incidentManager.assignTeamToIncident(selected.getId(), t.getId());
                    IncidentFileHandler.saveIncidents(incidentManager.getAllIncidents());
                    refreshTable.run();
                    loadAdminPriorityList(prioList);
                    stats.setText("Total: " + incidentManager.getAllIncidents().size());
                } catch (Exception ex) {
                    alert("Failed: " + ex.getMessage());
                }
            });
        });

        resolve.setOnAction(e -> {
            Incident selected = table.getSelectionModel().getSelectedItem();
            if (selected == null) {
                alert("Select an incident");
                return;
            }
            try {
                incidentManager.resolveIncident(selected.getId());
                IncidentFileHandler.saveIncidents(incidentManager.getAllIncidents());
                refreshTable.run();
                loadAdminPriorityList(prioList);
            } catch (Exception ex) {
                alert(ex.getMessage());
            }
        });

        showRep.setOnAction(e -> {
            Incident selected = table.getSelectionModel().getSelectedItem();
            if (selected == null) {
                alert("Select an incident");
                return;
            }
            showLargeText("Incident Report", selected.generateReport());
        });

        root.getChildren().addAll(controls, table);
        st.setScene(new Scene(root, 800, 500));
        st.show();
    }


    // -------------------------- REPORTS --------------------------
    private void showOverallReport() {
        List<Incident> all = incidentManager.getAllIncidents();
        StringBuilder sb = new StringBuilder();
        sb.append("Total: ").append(all.size()).append("\n");
        sb.append("Reported: ").append(incidentManager.getIncidentsByStatus("REPORTED").size()).append("\n");
        sb.append("Dispatched: ").append(incidentManager.getIncidentsByStatus("DISPATCHED").size()).append("\n");
        sb.append("Resolved: ").append(incidentManager.getIncidentsByStatus("RESOLVED").size()).append("\n\n");

        sb.append("---- Recent Incidents ----\n");
        all.stream().limit(20).forEach(i -> sb.append(i.generateReport()).append("\n"));

        // Display in themed window
        Stage st = new Stage();
        st.initModality(Modality.APPLICATION_MODAL);
        st.setTitle("Overall Report");

        TextArea t = new TextArea(sb.toString());
        t.setWrapText(true);
        t.setStyle("-fx-control-inner-background: white; -fx-font-family: 'Segoe UI';");

        VBox root = new VBox(10, t);
        root.setPadding(new Insets(10));
        root.setStyle("-fx-background-color: #dff5e1; -fx-background-radius: 10;");

        st.setScene(new Scene(root, 700, 600));
        st.show();
    }


    // -------------------------- USER DASHBOARD --------------------------
    private void showUserDashboard() {
        BorderPane root = new BorderPane();
        root.setPadding(new Insets(12));
        root.setStyle("-fx-background-color: #b8e6c1;");


        HBox top = new HBox(12);
        top.setAlignment(Pos.CENTER);
        Label lbl = new Label("User Dashboard");
        lbl.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: white;"); // white text
        top.getChildren().add(lbl);
        root.setTop(top);


        VBox left = new VBox(12);
        left.setPadding(new Insets(10));
        left.setAlignment(Pos.TOP_LEFT);


        Region spacer = new Region();
        VBox.setVgrow(spacer, Priority.ALWAYS);

        Button report = new Button("Report Incident");
        Button logout = new Button("Logout");

        left.getChildren().addAll(spacer, report, logout);
        root.setLeft(left);


        VBox center = new VBox(12);
        center.setPadding(new Insets(10));
        center.setStyle("-fx-background-color: #dff5e1; -fx-background-radius: 10;");
        center.setAlignment(Pos.TOP_CENTER);

        Label incidentsTitle = new Label("Active Incidents:");
        incidentsTitle.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
        incidentsTitle.setAlignment(Pos.CENTER);

        ListView<String> list = new ListView<>();
        refreshUserIncidentList(list);

        center.getChildren().addAll(incidentsTitle, list);
        root.setCenter(center);

        logout.setOnAction(e -> {
            auth.logout();
            showLoginScreen();
        });
        report.setOnAction(e -> showCreateIncident(list));

        primaryStage.setScene(new Scene(root, 800, 500));
    }


    // -------------------------- CREATE INCIDENT --------------------------
    private void actuallyShowCreateIncident(ListView<String> list) {
        Stage st = new Stage();
        st.initModality(Modality.APPLICATION_MODAL);
        st.setTitle("Report Incident");

        HBox root = new HBox(12);
        root.setPadding(new Insets(12));
        root.setStyle("-fx-background-color: #dff5e1;");

        ComboBox<String> type = new ComboBox<>();
        type.getItems().addAll("Accident", "Construction", "Weather Alert");
        type.setValue("Accident");
        type.setStyle("-fx-background-color: #b8e6c1;");

        TextField locField = new TextField();
        locField.setPromptText("Click map to choose location ");

        Button pickLoc = new Button("Open Map");
        pickLoc.setOnAction(e -> {
            MapPicker picker = new MapPicker();
            picker.showMapPicker(locField);
            selectedLat = picker.getSelectedLat();
            selectedLng = picker.getSelectedLng();
            selectedAddress = picker.getSelectedAddress();
        });
        pickLoc.setStyle("-fx-background-color: #b8e6c1; -fx-font-weight: bold;");

        TextArea desc = new TextArea();
        desc.setPromptText("Description");

        VBox vehBox = new VBox(8);
        Label lv = new Label("Vehicles:");
        Spinner<Integer> spinVeh = new Spinner<>(0, 50, 1);
        spinVeh.setStyle("-fx-background-color: #b8e6c1;");
        vehBox.getChildren().addAll(lv, spinVeh);

        CheckBox injuries = new CheckBox("Injuries reported");

        Label blockage = new Label("Road Blockage Level:");
        ComboBox<RoadBlockageLevel> block = new ComboBox<>();
        block.getItems().addAll(RoadBlockageLevel.values());
        block.setValue(RoadBlockageLevel.NONE);
        block.setStyle("-fx-background-color: #b8e6c1;");

        Label severity = new Label("Severity:");
        ComboBox<Severity> sev = new ComboBox<>();
        sev.getItems().addAll(Severity.values());
        sev.setValue(Severity.MEDIUM);
        sev.setStyle("-fx-background-color: #b8e6c1;");

        VBox svcBox = new VBox(5);
        Label svcLbl = new Label("Emergency services:");
        CheckBox amb = new CheckBox("Ambulance 🚑");
        CheckBox fire = new CheckBox("Fire 🔥");
        CheckBox police = new CheckBox("Police 🚨");
        CheckBox other = new CheckBox("Other:");
        TextField otherField = new TextField();
        svcBox.getChildren().addAll(svcLbl, amb, fire, police, other, otherField);

        Button submit = new Button("Submit");
        submit.setStyle("-fx-background-color: #b8e6c1; -fx-font-weight: bold;");

        submit.setOnAction(e -> {

            int nextId = incidentManager.getAllIncidents().size() + 1;
            String d = desc.getText().trim();
            if (d.isEmpty()) d = type.getValue() + " reported";

            String addr = locField.getText().isEmpty()
                    ? String.format("%.5f, %.5f", selectedLat, selectedLng)
                    : locField.getText();

            Location loc = new Location(selectedLat, selectedLng, addr);

            Incident inc = null;
            showUserDashboard();


            if (type.getValue().equals("Accident")) {
                List<String> needed = new ArrayList<>();
                if (amb.isSelected()) needed.add("Ambulance");
                if (fire.isSelected()) needed.add("Fire");
                if (police.isSelected()) needed.add("Police");
                if (other.isSelected() && !otherField.getText().isBlank())
                    needed.add(otherField.getText().trim());

                inc = new Accident(
                        nextId, d, loc, sev.getValue(),
                        spinVeh.getValue(), injuries.isSelected(),
                        needed, block.getValue()
                );


            } else if (type.getValue().equals("Construction")) {
                inc = new Construction(nextId, d, loc,
                        System.currentTimeMillis() + 30L * 24 * 60 * 60 * 1000);

            } else if (type.getValue().equals("Weather Alert")) {
                VisibilityInfo vis = WeatherService.getVisibilityInfo(
                        loc.getLat(), loc.getLng());
                if (vis == null) vis = new VisibilityInfo(1000, 0, 0);
                inc = new WeatherAlert(nextId, d, loc, vis);
            }

            if (inc != null) {
                incidentManager.createIncident(inc);
                IncidentFileHandler.saveIncidents(incidentManager.getAllIncidents());
                st.close();
                showUserDashboard();
            }

        });


        GridPane g = new GridPane();
        g.setHgap(20);
        g.setVgap(20);
        g.setStyle("-fx-background-color: white; -fx-padding: 10; -fx-border-radius: 5; -fx-background-radius: 5;");

        int r = 0;
        g.add(new Label("Type:"), 0, r);
        g.add(type, 1, r++);
        g.add(new Label("Location:"), 0, r);
        g.add(locField, 1, r++);
        g.add(pickLoc, 1, r++);
        g.add(new Label("Description:"), 0, r);
        g.add(desc, 1, r++);
        g.add(vehBox, 1, r++);
        g.add(injuries, 1, r++);
        g.add(blockage, 0, r);
        g.add(block, 1, r++);
        g.add(severity, 0, r);
        g.add(sev, 1, r++);
        g.add(svcBox, 0, r, 2, 1);
        g.add(submit, 0, ++r);

        type.setOnAction(ev -> {
            boolean acc = type.getValue().equals("Accident");
            vehBox.setVisible(acc);
            injuries.setVisible(acc);
            blockage.setVisible(acc);
            block.setVisible(acc);
            severity.setVisible(acc);
            sev.setVisible(acc);
            svcBox.setVisible(acc);
        });

        type.getOnAction().handle(null);

        root.getChildren().add(g);
        st.setScene(new Scene(root, 650, 600));
        st.show();


    }
    private void showCreateIncident(ListView<String> list) {
        Image ambulanceImg = new Image(getClass().getResource("/images/ambulance2.png").toString());
        showLoadingScreen(ambulanceImg, () -> actuallyShowCreateIncident(list));
    }


    // -------------------------- MAP PICKER HELPER --------------------------
    public static class MapPicker {

        private double selectedLat;
        private double selectedLng;
        private String selectedAddress;

        public void showMapPicker(TextField locField) {

            Stage popup = new Stage();
            popup.setTitle("Pick Location");
            popup.initModality(Modality.APPLICATION_MODAL);

            VBox root = new VBox(10);
            root.setPadding(new Insets(10));

            WebView mapView = new WebView();
            mapView.setPrefSize(800, 550);

            Button selectBtn = new Button("Select Location");
            selectBtn.setDisable(true);

            WebEngine engine = mapView.getEngine();
            engine.loadContent(MapService.getMapHtml());

            mapView.setOnMouseClicked(e -> selectBtn.setDisable(false));

            selectBtn.setOnAction(e -> {
                double[] coords = MapService.getSelectedCoordinates(engine);
                if (coords != null) {
                    selectedLat = coords[0];
                    selectedLng = coords[1];
                    selectedAddress = "Selected location (" +
                            String.format("%.5f", selectedLat) + ", " +
                            String.format("%.5f", selectedLng) + ")";
                    locField.setText(selectedAddress);
                    popup.close();
                }
            });

            root.getChildren().addAll(mapView, selectBtn);

            Scene scene = new Scene(root, 820, 600);
            popup.setScene(scene);
            popup.showAndWait();
        }

        public double getSelectedLat() { return selectedLat; }
        public double getSelectedLng() { return selectedLng; }
        public String getSelectedAddress() { return selectedAddress; }
    }

    // -------------------------- UTILS --------------------------
    private void alert(String msg) {
        Alert a = new Alert(Alert.AlertType.INFORMATION, msg, ButtonType.OK);
        a.showAndWait();
    }

    private void showLargeText(String title, String txt) {
        Stage st = new Stage();
        st.initModality(Modality.APPLICATION_MODAL);
        st.setTitle(title);
        TextArea t = new TextArea(txt);
        t.setWrapText(true);
        VBox root = new VBox(10, t);
        root.setPadding(new Insets(10));
        st.setScene(new Scene(root, 700, 600));
        st.show();
    }
    // -------------------------- HELPER: load priority incidents --------------------------
    private void loadAdminPriorityList(ListView<String> view) {
        List<Incident> list = incidentManager.getActiveIncidents();
        List<String> display = new ArrayList<>();
        for (Incident i : list) {
            String addr = i.getLocation() == null ? "N/A" : i.getLocation().getAddress();
            display.add(i.getType() + " (ID:" + i.getId() + ") - " + addr);
        }
        view.setItems(FXCollections.observableArrayList(display));
    }
    private void refreshTeamList(ListView<String> v) {
        List<String> out = new ArrayList<>();
        for (Team t : incidentManager.getAllTeams()) {
            out.add(t.toString());
        }
        v.setItems(FXCollections.observableArrayList(out));
    }
    // -------------------------- HELPER: FILTER INCIDENTS --------------------------
    private List<Incident> applyFilter(String f) {
        if (f == null || f.equals("All"))
            return new ArrayList<>(incidentManager.getAllIncidents());
        return new ArrayList<>(incidentManager.getIncidentsByStatus(f));
    }
    private void refreshUserIncidentList(ListView<String> list) {
        List<String> out = new ArrayList<>();
        for (Incident i : incidentManager.getActiveIncidents()) {
            out.add(i.getType() + " - " +
                    (i.getLocation() == null ? "N/A" : i.getLocation().getAddress()));
        }
        list.setItems(FXCollections.observableArrayList(out));
    }

}