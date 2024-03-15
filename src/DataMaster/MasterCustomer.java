package DataMaster;

import DBConnect.DBConnect;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class MasterCustomer extends JFrame{
    public JPanel JPCustomer;
    private JPanel JPanel2;
    private javax.swing.JPanel JPanel;
    private JTextField txtID;
    private JTable tableView;
    private JButton btnSimpan;
    private JButton btnUbah;
    private JButton btnHapus;
    private JButton btnBatal;
    private JTextField txtCari;
    private JButton btnCari;
    private JTextField txtNama;
    private JTextField txtEmail;
    private JTextField txtNotelp;
    private JPanel jpCald;
    String autokode,id,nama,email,notelp,tanggal;
    DBConnect connection = new DBConnect();
    private DefaultTableModel model = new DefaultTableModel();

    public MasterCustomer() {
        setContentPane(JPCustomer);
        tableView.setModel(model);
        addColomn();
        loadData();
        autokode();

        btnSimpan.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //validasi email
                String PATTERN = "^[a-zA-Z0-9]{0,30}[@][a-zA-Z0-9]{0,10}[.][a-zA-Z]{0,5}$";
                Pattern patt = Pattern.compile(PATTERN);
                Matcher match = patt.matcher(txtEmail.getText());
                id = txtID.getText();
                nama = txtNama.getText();
                email = txtEmail.getText();
                notelp = txtNotelp.getText();
                try {
                    if (nama.equals("") || email.equals("") || notelp.equals("")) {
                        JOptionPane.showMessageDialog(null, "Mohon untuk mengisi seluruh data!");
                    } else if (!match.matches()) {
                        JOptionPane.showMessageDialog(null, "Masukkan Email yang benar!");
                        txtEmail.setText("");
                    } else if (txtNotelp.getText().length() > 13 || txtNotelp.getText().length() < 8){
                        JOptionPane.showMessageDialog(null, "Panjang digit no telepon anda tidak memenuhi standar!");
                        txtNotelp.setText("");
                    }else {
                        try {
                            String query = "EXEC sp_InsertCustomer @Id_Customer=?, @Nama_Customer=?, @Email_Customer=?," +
                                    "@NoTelp_Customer=?";
                            connection.pstat = connection.conn.prepareStatement(query);
                            connection.pstat.setString(1, id);
                            connection.pstat.setString(2, nama);
                            connection.pstat.setString(3, email);
                            connection.pstat.setString(4, notelp);
                            connection.pstat.executeUpdate();
                            connection.pstat.close();
                            JOptionPane.showMessageDialog(null, "Insert data berhasil");
                            reset();
                        } catch (Exception ex) {
                            System.out.println("Error saat inssert data ke database" + ex);
                        }
                    }
                    loadData();
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
                nama = txtNama.getText();
                email = txtEmail.getText();
                notelp = txtNotelp.getText();
                try {
                    if (nama.equals("") || email.equals("") || notelp.equals("")) {
                        JOptionPane.showMessageDialog(null, "Mohon untuk mengisi seluruh data!");
                    } else if (!match.matches()) {
                        JOptionPane.showMessageDialog(null, "Masukkan Email yang benar!");
                        txtEmail.setText("");
                    } else if (txtNotelp.getText().length() > 13 || txtNotelp.getText().length() < 8){
                        JOptionPane.showMessageDialog(null, "Panjang digit no telepon anda tidak memenuhi standar!");
                        txtNotelp.setText("");
                    } else {
                        try {
                            String query = "EXEC sp_UpdateCustomer @Id_Customer=?, @Nama_Customer=?, @Email_Customer=?, @NoTelp_Customer=?";
                            connection.pstat = connection.conn.prepareStatement(query);
                            connection.pstat.setString(1, id);
                            connection.pstat.setString(2, nama);
                            connection.pstat.setString(3, email);
                            connection.pstat.setString(4, notelp);
                            connection.pstat.executeUpdate();
                            connection.pstat.close();
                            JOptionPane.showMessageDialog(null, "Data berhasil diubah");
                            reset();
                        } catch (Exception ex) {
                            System.out.println("Error saat update data ke database" + ex);
                        }
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, ex.getMessage());
                }
            }
        });
        btnHapus.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int opsi;
                id = txtID.getText();
                if (txtNama.getText().equals("")) {
                    JOptionPane.showMessageDialog(null, "Input data yang ingin dihapus!", "Peringatan",
                            JOptionPane.WARNING_MESSAGE);
                } else {
                    try {
                        opsi = JOptionPane.showConfirmDialog(null, "Apakah ingin menghapus data?",
                                "Konfirmasi", JOptionPane.YES_OPTION, JOptionPane.WARNING_MESSAGE);
                        if (opsi != 0) {
                            JOptionPane.showMessageDialog(null, "Data Tidak Berhasil Dihapus");
                        } else {
                            String query = "EXEC sp_DeleteCustomer @Id_Customer=?";
                            connection.pstat = connection.conn.prepareStatement(query);
                            connection.pstat.setString(1, id);
                            connection.pstat.executeUpdate();
                            connection.pstat.close();
                            JOptionPane.showMessageDialog(null, "Data Berhasil Dihapus");
                            reset();
                        }
                    } catch (Exception ex) {
                        System.out.println("Error pada saat delete data" + ex);
                    }
                }
            }
        });

        btnBatal.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                reset();
            }
        });
        btnCari.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                model.getDataVector().removeAllElements(); //menghapus semua data ditamp
                model.fireTableDataChanged(); // memberitahu data telah ksong

                try {
                    DBConnect connection = new DBConnect();
                    connection.stat = connection.conn.createStatement();
                    String query = "SELECT * FROM Customer WHERE Id_Customer = '" + txtCari.getText() + "' AND Status = 1";
                    connection.result = connection.stat.executeQuery(query);

                    //lakukan baris perbari data
                    while (connection.result.next()) {
                        Object[] obj = new Object[5];
                        obj[0] = connection.result.getString(1);
                        obj[1] = connection.result.getString(2);
                        obj[2] = connection.result.getString(3);
                        obj[3] = connection.result.getString(4);
                        model.addRow(obj);
                    }
                    if (model.getRowCount() == 0) {
                        JOptionPane.showMessageDialog(null, "Data tidak ditemukan");
                    }
                    connection.stat.close();
                    connection.result.close();
                } catch (Exception ex) {
                    System.out.println("Error saat pencarian data" + ex);
                }
            }
        });
        tableView.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                int i = tableView.getSelectedRow();
                txtID.setText((String) model.getValueAt(i, 0));
                txtNama.setText((String) model.getValueAt(i, 1));
                txtEmail.setText((String) model.getValueAt(i, 2));
                txtNotelp.setText((String) model.getValueAt(i, 3));
            }
        });
        txtNama.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                super.keyTyped(e);
                char nama = e.getKeyChar();
                if (!(Character.isAlphabetic(nama))) e.consume();
            }
        });

        txtNotelp.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                super.keyTyped(e);
                char telp = e.getKeyChar();
                if (!(Character.isDigit(telp))) e.consume();
            }
        });
        txtNama.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                super.keyTyped(e);
                char nama = e.getKeyChar();
                if (!(Character.isAlphabetic(nama))) e.consume();
            }
        });
    }

    public void autokode(){
        try{
            String sql = "SELECT * FROM Customer ORDER BY Id_Customer desc";
            connection.stat = connection.conn.createStatement();
            connection.result = connection.stat.executeQuery(sql);
            if (connection.result.next()) {
                autokode = connection.result.getString("Id_Customer").substring(4);
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
                txtID.setText("CM" + nol + AN);
                txtID.setEnabled(false);

            }else {
                txtID.setText("CM0001");
                txtID.setEnabled(false);
            }
            connection.stat.close();
            connection.result.close();
        }catch (Exception e1){
            System.out.println("Terjadi error pada CRUD: " + e1);
        }
    }

    public void loadData(){
        model.getDataVector().removeAllElements();
        model.fireTableDataChanged();
        try{
            DBConnect connection = new DBConnect();
            connection.stat = connection.conn.createStatement();
            String query = "EXEC sp_LoadCustomer";
            connection.result = connection.stat.executeQuery(query);
            while (connection.result.next()){
                Object[] obj = new Object[5];
                obj[0] = connection.result.getString(1);
                obj[1] = connection.result.getString(2);
                obj[2] = connection.result.getString(3);
                obj[3] = connection.result.getString(4);
                model.addRow(obj);
            }
            connection.stat.close();
            connection.result.close();
        }catch (Exception ex){
            System.out.println("Terjadi error saat load data" +ex);
        }
    }

    public void addColomn(){
        model.addColumn("ID Customer");
        model.addColumn("Nama Customer");
        model.addColumn("Email Customer");
        model.addColumn("Nomor Telepon");
    }

    public void clear(){
        loadData();
        txtNama.setText("");
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
