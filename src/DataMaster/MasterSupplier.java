package DataMaster;

import DBConnect.DBConnect;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.*;
import java.sql.SQLException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class MasterSupplier extends JFrame{
    private JScrollPane scrollpanel;
    public JPanel JPSupplier;
    private JTextField txtID;
    private JTextField txtEmail;
    private JTextField txtNama;
    private JTextField txtNotelp;
    private JButton btnSimpan;
    private JButton btnUbah;
    private JButton btnHapus;
    private JButton btnBatal;
    private JTextField txtCari;
    private JButton btnCari;
    private JComboBox<String> cbMerk;
    private JTable tableView;
    DBConnect connection = new DBConnect();
    private DefaultTableModel model = new DefaultTableModel();

    //variable
    String id, merk, nama, email, telp, autokode;

    public MasterSupplier(){
        tampilMerk();
        model = new DefaultTableModel();
        tableView.setModel(model);
        addColomn();
        autokode();
        loadData();
        cbMerk.setSelectedItem(null);

        btnSimpan.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String PATTERN = "^[a-zA-Z0-9]{0,30}[@][a-zA-Z0-9]{0,10}[.][a-zA-Z]{0,5}$";
                Pattern patt = Pattern.compile(PATTERN);
                Matcher match = patt.matcher(txtEmail.getText());
                id = txtID.getText();
                nama = txtNama.getText();
                email = txtEmail.getText();
                telp = txtNotelp.getText();
                try {

                    if (nama.equals("") | cbMerk.equals("") | email.equals("") | telp.equals("") ) {
                        throw new Exception("Mohon untuk mengisi seluruh data!");
                    } else if (!match.matches()) {
                        JOptionPane.showMessageDialog(null, "Masukkan Email yang benar!");
                        txtEmail.setText("");
                    }else if(telp.length() < 8 || telp.length() > 13){
                        throw new Exception("Panjang digit no telepon anda tidak memenuhi standar!");
                    } else {
                        try {
                            String.valueOf(cbMerk.getSelectedItem());
                            tampilIdMerk();
                            String query = "EXEC sp_InsertSupplier @Id_Supplier=?, @Id_Merk=?, @Nama_Supplier=?, @Email_Supplier=?, @NoTelp_Supplier=?";
                            connection.pstat = connection.conn.prepareStatement(query);
                            connection.pstat.setString(1, id);
                            connection.pstat.setString(2, merk);
                            connection.pstat.setString(3, nama);
                            connection.pstat.setString(4, email);
                            connection.pstat.setString(5, telp);
                            connection.pstat.executeUpdate();
                            connection.pstat.close();
                            JOptionPane.showMessageDialog(null, "Insert data berhasil");
                            clear();
                            reset();
                        } catch (Exception ex) {
                            System.out.println("Error saat insert data ke database" + ex);
                        }
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, ex.getMessage());
                }
            }
        });


        btnUbah.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String PATTERN = "^[a-zA-Z0-9]{0,30}[@][a-zA-Z0-9]{0,10}[.][a-zA-Z]{0,5}$";
                Pattern patt = Pattern.compile(PATTERN);
                Matcher match = patt.matcher(txtEmail.getText());
                id = txtID.getText();
                cbMerk.getSelectedItem();
                nama = txtNama.getText();
                telp = txtNotelp.getText();
                email = txtEmail.getText();
                try {
                    if (nama.equals("") | telp.equals("") | cbMerk.equals("") | email.equals(""))
                    {
                        throw new Exception("Mohon untuk mengisi seluruh data!");
                    } else if (!match.matches()) {
                        JOptionPane.showMessageDialog(null, "Masukkan Email yang benar!");
                        txtEmail.setText("");
                    } else if(telp.length() < 8 || telp.length() > 13) {
                        throw new Exception("Panjang digit no telepon anda tidak memenuhi standar!");
                    }else {
                        try {
                            String.valueOf(cbMerk.getSelectedItem());
                            tampilIdMerk();
                            String query = "EXEC sp_UpdateSupplier @Id_Merk=?, @Nama_Supplier=?, @Email_Supplier=?, @NoTelp_Supplier=? ,@Id_Supplier=?";
                            connection.pstat = connection.conn.prepareStatement(query);
                            connection.pstat.setString(5, id);
                            connection.pstat.setString(1, merk);
                            connection.pstat.setString(2, nama);
                            connection.pstat.setString(3, email);
                            connection.pstat.setString(4, telp);
                            connection.pstat.executeUpdate();
                            connection.pstat.close();
                            JOptionPane.showMessageDialog(null, "Data Berhasil Diubah");
                            reset();
                            clear();
                        } catch (Exception ex) {
                            System.out.println("Error saat update data ke database" + ex);
                        }
                    }
                }catch (Exception ex){
                    JOptionPane.showMessageDialog(null,ex.getMessage());
                }
            }
        });

        tableView.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                int i = tableView.getSelectedRow();
                txtID.setText((String) model.getValueAt(i,0));
                txtNama.setText((String) model.getValueAt(i,1));
                txtEmail.setText((String) model.getValueAt(i,2));
                txtNotelp.setText((String) model.getValueAt(i,3));
                cbMerk.setSelectedItem((String) model.getValueAt(i,4));
            }
        });

        btnHapus.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int opsi;
                id = txtID.getText();
                if (txtNama.getText().equals("")){
                    JOptionPane.showMessageDialog(null, "Input data yang ingin dihapus!", "Peringatan",
                            JOptionPane.WARNING_MESSAGE);
                } else {
                    try {
                        opsi = JOptionPane.showConfirmDialog(null, "Apakah ingin menghapus data?",
                                "Konfirmasi", JOptionPane.YES_OPTION, JOptionPane.WARNING_MESSAGE);
                        if (opsi != 0) {
                            JOptionPane.showMessageDialog(null, "Data Tidak Berhasil Dihapus");
                        } else {
                            String query = "EXEC sp_DeleteSupplier @Id_Supplier=?";
                            connection.pstat = connection.conn.prepareStatement(query);
                            connection.pstat.setString(1, id);
                            connection.pstat.executeUpdate();
                            connection.pstat.close();
                            JOptionPane.showMessageDialog(null, "Data Berhasil Dihapus");
                        }
                    } catch (Exception ex) {
                        System.out.println("Error pada saat delete data" + ex);
                    }
                    reset();
                }
            }
        });
        btnBatal.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                txtCari.setText("");
                reset();
            }
        });


        btnCari.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                id = txtCari.getText();
                model.getDataVector().removeAllElements(); //menghapus semua data ditamp
                model.fireTableDataChanged(); // memberitahu data telah ksong

                try{
                    DBConnect connection = new DBConnect();
                    connection.stat = connection.conn.createStatement();
                    String query = "EXEC sp_CariSupplier @Id_Supplier=?";
                    connection.pstat = connection.conn.prepareStatement(query);
                    connection.pstat.setString(1, id);
                    connection.result = connection.pstat.executeQuery();

                    //lakukan baris perbari data
                    while (connection.result.next()) {
                        Object[] obj = new Object[5];
                        obj[0] = connection.result.getString(1);
                        obj[1] = connection.result.getString(2);
                        obj[2] = connection.result.getString(3);
                        obj[3] = connection.result.getString(4);
                        obj[4] = connection.result.getString(5);
                        model.addRow(obj);
                    }
                    if(model.getRowCount() == 0){
                        JOptionPane.showMessageDialog(null, "Data tidak ditemukan");
                    }
                    connection.stat.close();
                    connection.result.close();
                }catch (Exception ex){
                    System.out.println("Error saat pencarian data" +ex);
                }
            }
        });

        txtNotelp.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                super.keyTyped(e);
                char telp = e.getKeyChar();
                if (!(Character.isDigit(telp))) e.consume();
                else if (txtNotelp.getText().length() >= 13) e.consume();
            }
        });
    }

    public void autokode(){
        try{
            String sql = "SELECT * FROM Supplier ORDER BY Id_Supplier desc";
            connection.stat = connection.conn.createStatement();
            connection.result = connection.stat.executeQuery(sql);
            if (connection.result.next()) {
                autokode = connection.result.getString("Id_Supplier").substring(4);
                String AN = "" + (Integer.parseInt(autokode) + 1);
                String nol = "";

                if (AN.length() == 1) {
                    nol = "000";
                } else if (AN.length() == 2) {
                    nol = "00";
                } else if (AN.length() == 3) {
                    nol = "0";
                }else if (AN.length() == 4) {
                    nol = "";
                }
                txtID.setText("SP" + nol + AN);
                txtID.setEnabled(false);

            }else {
                txtID.setText("SP0001");
                txtID.setEnabled(false);
            }
            connection.stat.close();
            connection.result.close();
        }catch (Exception e1){
            System.out.println("Terjadi error pada CRUD: " + e1);
        }
    }

    public void tampilMerk() {
        try {
            DBConnect connection = new DBConnect();
            connection.stat = connection.conn.createStatement();
            String sql1 = "SELECT Nama_Merk FROM Merk WHERE Status = 1";
            connection.result = connection.stat.executeQuery(sql1);

            while (connection.result.next()) {
                cbMerk.addItem(connection.result.getString("Nama_Merk"));
            }
            connection.stat.close();
            connection.result.close();
        } catch (SQLException ex) {
            System.out.println("Terjadi error saat load data ID Merk" + ex);
        }
    }

    public void tampilIdMerk() {
        try {
            DBConnect connection = new DBConnect();
            connection.stat = connection.conn.createStatement();
            String sql1 = "SELECT Id_Merk FROM Merk WHERE Nama_Merk LIKE '%" +
                    String.valueOf(cbMerk.getSelectedItem()) + "%'";
            connection.result = connection.stat.executeQuery(sql1);

            while (connection.result.next()) {
                merk = (connection.result.getString("Id_Merk"));
            }
            connection.stat.close();
            connection.result.close();
        } catch (SQLException ex) {
            System.out.println("Terjadi error saat load data" + ex);
        }
    }

    public void loadData(){
        model.getDataVector().removeAllElements();
        model.fireTableDataChanged();
        try{
            DBConnect connection = new DBConnect();
            connection.stat = connection.conn.createStatement();
            String query = "EXEC sp_LoadSupplier";
            connection.result = connection.stat.executeQuery(query);
            while (connection.result.next()){
                Object[] obj = new Object[5];
                obj[0] = connection.result.getString(1);
                obj[1] = connection.result.getString(2);
                obj[2] = connection.result.getString(3);
                obj[3] = connection.result.getString(4);
                obj[4] = connection.result.getString(5);
                model.addRow(obj);
            }
            connection.stat.close();
            connection.result.close();
        }catch (Exception ex){
            System.out.println("Terjadi error saat load data" +ex);
        }
    }

    public void addColomn(){
        model.addColumn("ID Supplier");
        model.addColumn("Nama Supplier");
        model.addColumn("Email Supplier");
        model.addColumn("No.Telp Supplier");
        model.addColumn("Merk");
    }

    public void clear(){
        cbMerk.setSelectedItem("");
        txtNama.setText("");
        txtEmail.setText("");
        txtNotelp.setText("");
    }
    public void reset() {
        clear();
        loadData();
        autokode();
    }
}
