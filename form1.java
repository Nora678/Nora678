import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class form1 {
    private JFrame frame;
    private JTable table;
    private DefaultTableModel tableModel;
    private JTextField firstNameField, lastNameField, phoneField, emailField, addressField;
    private JComboBox<String> genderBox;
    private JButton addButton, deleteButton;

    public form1() {
        frame = new JFrame("Phonebook Application");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setLayout(new BorderLayout());

        tableModel = new DefaultTableModel(new String[]{"id", "firstName", "lastName", "phone", "email", "address", "gender"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column != 0;
            }
        };
        table = new JTable(tableModel);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane tableScrollPane = new JScrollPane(table);


        tableModel.addTableModelListener(new TableModelListener() {
            public void tableChanged(TableModelEvent e) {
                if (e.getType() == TableModelEvent.UPDATE) {
                    int row = e.getFirstRow();
                    int column = e.getColumn();
                    String columnName = tableModel.getColumnName(column);
                    Object newValue = tableModel.getValueAt(row, column);
                    int id = (int) tableModel.getValueAt(row, 0);
                    connect.updateContactColumn(id, columnName, newValue.toString());
                }
            }
        });


        JPanel inputPanel = new JPanel(new GridLayout(3, 4, 10, 10));
        firstNameField = new JTextField();
        lastNameField = new JTextField();
        phoneField = new JTextField();
        emailField = new JTextField();
        addressField = new JTextField();
        genderBox = new JComboBox<>(new String[]{"Male", "Female", "Other"});

        inputPanel.add(new JLabel("First Name:")); inputPanel.add(firstNameField);
        inputPanel.add(new JLabel("Last Name:")); inputPanel.add(lastNameField);
        inputPanel.add(new JLabel("Phone:")); inputPanel.add(phoneField);
        inputPanel.add(new JLabel("Email:")); inputPanel.add(emailField);
        inputPanel.add(new JLabel("Address:")); inputPanel.add(addressField);
        inputPanel.add(new JLabel("Gender:")); inputPanel.add(genderBox);

        addButton = new JButton("Add Contact");
        deleteButton = new JButton("Delete Contact");

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(addButton);
        buttonPanel.add(deleteButton);

        addButton.addActionListener(e -> addContact());
        deleteButton.addActionListener(e -> deleteContact());

        frame.add(tableScrollPane, BorderLayout.CENTER);
        frame.add(inputPanel, BorderLayout.NORTH);
        frame.add(buttonPanel, BorderLayout.SOUTH);

        refreshTable();
        frame.setVisible(true);
    }

    private void addContact() {
        if (!validateFields()) return;

        connect.addContact(
                firstNameField.getText().trim(),
                lastNameField.getText().trim(),
                phoneField.getText().trim(),
                emailField.getText().trim(),
                addressField.getText().trim(),
                genderBox.getSelectedItem().toString()
        );

        refreshTable();
        clearFields();
    }

    private void deleteContact() {
        int row = table.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(frame, "Select a contact to delete.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int id = Integer.parseInt(tableModel.getValueAt(row, 0).toString());
        connect.deleteContact(id);
        refreshTable();
        clearFields();
    }

    private void refreshTable() {
        tableModel.setRowCount(0);
        try {
            ResultSet rs = connect.getAllContacts();
            while (rs.next()) {
                tableModel.addRow(new Object[]{
                        rs.getInt("id"),
                        rs.getString("firstName"),
                        rs.getString("lastName"),
                        rs.getString("phone"),
                        rs.getString("email"),
                        rs.getString("address"),
                        rs.getString("gender")
                });
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(frame, "Error loading contacts: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private boolean validateFields() {
        String firstName = firstNameField.getText().trim();
        String phone = phoneField.getText().trim();
        String email = emailField.getText().trim();


        if (firstName.isEmpty() || phone.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Required fields missing!", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (!phone.matches("\\d+")) {
            JOptionPane.showMessageDialog(frame, "Invalid phone number!", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (!email.isEmpty() && (!email.contains("@") || email.contains(" "))) {
            JOptionPane.showMessageDialog(frame, "Invalid email!", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        return true;
    }


    private void clearFields() {
        firstNameField.setText("");
        lastNameField.setText("");
        phoneField.setText("");
        emailField.setText("");
        addressField.setText("");
        genderBox.setSelectedIndex(0);
    }

}
