package DataMaster;

import DBConnect.DBConnect;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.*;
import java.sql.SQLException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MasterKaryawan extends JFrame{
    public JPanel JPKaryawan;
    private JPanel JPanel;
    private JTextField txtID;
    private JTable tableView;
    private JButton btnSimpan;
    private JButton btnUbah;
    private JButton btnHapus;
    private JButton btnBatal;
    private JTextField txtCari;
    private JButton btnCari;
    private JComboBox cbJabatan;
    private JRadioButton rbLaki;
    private JRadioButton rbPerempuan;
    private JTextField txtNama;
    private JTextField txtEmail;
    private JTextField txtNotelp;
    private JTextField txtUsername;
    private JTextField txtPassword;
    DBConnect connection = new DBConnect();
    private DefaultTableModel model = new DefaultTableModel();

    //variabel
    String id,jabatan,nama,email,notelp,jenkil,user,pass,autokode;
    int status;

    public MasterKaryawan(){
        tampilJabatan();
        model = new DefaultTableModel();
        tableView.setModel(model);
        addColomn();
        loadData();
        clear();
        FrameRadioButton();
        autokode();
        cbJabatan.setSelectedItem(null);

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
                user = txtUsername.getText();
                pass = txtPassword.getText();
                String.valueOf(cbJabatan.getSelectedItem());
                tampilIdJabatan();
                if (rbLaki.isSelected()){
                    jenkil = "Laki Laki";
                }else if (rbPerempuan.isSelected()){
                    jenkil = "Perempuan";
                }else{
                    jenkil = "";
                }
                try {
                    if(id.equals("") | nama.equals("") | email.equals("") | notelp.equals("")|jenkil.equals(""))
                    {
                        throw new Exception("Mohon untuk mengisi seluruh data!");
                    } else if (!match.matches()) {
                        JOptionPane.showMessageDialog(null, "Masukkan Email yang benar!");
                    }else if (txtNotelp.getText().length() > 13 || txtNotelp.getText().length() < 8) {
                        JOptionPane.showMessageDialog(null, "Panjang digit no telepon anda tidak memenuhi standar!");
                        txtNotelp.setText("");
                    }else {
                        try {
                            String query = "EXEC sp_InsertKaryawan @Id_Karyawan=?, @Id_Jabatan=?, @Nama_Karyawan=?, @Email_Karyawan=?, @NoTelp_Karyawan=?," +
                                    "@Jenis_Kelamin=?, @Username=?, @Password=?";
                            connection.pstat = connection.conn.prepareStatement(query);
                            connection.pstat.setString(1, id);
                            connection.pstat.setString(2, jabatan);
                            connection.pstat.setString(3, nama);
                            connection.pstat.setString(4, email);
                            connection.pstat.setString(5, notelp);
                            connection.pstat.setString(6, jenkil);
                            connection.pstat.setString(7, user);
                            connection.pstat.setString(8, pass);
                            connection.pstat.executeUpdate();
                            connection.pstat.close();
                            JOptionPane.showMessageDialog(null, "Insert data berhasil");
                            clear();
                            reset();
                            autokode();
                        } catch (Exception ex) {
                            System.out.println("Error saat inssert data ke database" + ex);
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
                //validasi email
                String PATTERN = "^[a-zA-Z0-9]{0,30}[@][a-zA-Z0-9]{0,10}[.][a-zA-Z]{0,5}$";
                Pattern patt = Pattern.compile(PATTERN);
                Matcher match = patt.matcher(txtEmail.getText());
                id = txtID.getText();
                nama = txtNama.getText();
                email = txtEmail.getText();
                notelp = txtNotelp.getText();
                user = txtUsername.getText();
                pass = txtPassword.getText();
                if (rbLaki.isSelected()){
                    jenkil = "Laki Laki";
                }else if (rbPerempuan.isSelected()){
                    jenkil = "Perempuan";
                }else{
                    jenkil = "";
                }
                try {
                    if(id.equals("") | nama.equals("") | email.equals("") | notelp.equals("")| jenkil.equals(""))
                    {
                        throw new Exception("Mohon untuk mengisi seluruh data!");
                    } else if (!match.matches()) {
                        JOptionPane.showMessageDialog(null, "Masukkan Email yang benar!");
                    }else if (txtNotelp.getText().length() > 13 || txtNotelp.getText().length() < 8) {
                        JOptionPane.showMessageDialog(null, "Panjang digit no telepon anda tidak memenuhi standar!");
                        txtNotelp.setText("");
                    } else {
                        try {
                            String.valueOf(cbJabatan.getSelectedItem());
                            tampilIdJabatan();
                            String query = "UPDATE Karyawan SET Id_Jabatan=?, Nama_Karyawan=?, Email_Karyawan=?, NoTelp_Karyawan=?," +
                                    "Jenis_Kelamin=?, Username=?, Password=? WHERE Id_Karyawan=?";
                            connection.pstat = connection.conn.prepareStatement(query);
                            connection.pstat.setString(8, id);
                            connection.pstat.setString(1, jabatan);
                            connection.pstat.setString(2, nama);
                            connection.pstat.setString(3, email);
                            connection.pstat.setString(4, notelp);
                            connection.pstat.setString(5, jenkil);
                            connection.pstat.setString(6, user);
                            connection.pstat.setString(7, pass);
                            connection.pstat.executeUpdate();
                            connection.pstat.close();
                            JOptionPane.showMessageDialog(null, "Data Berhasil Diubah");
                            reset();
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
                cbJabatan.setSelectedItem((String) model.getValueAt(i,1));
                txtNama.setText((String) model.getValueAt(i,2));
                txtEmail.setText((String) model.getValueAt(i,3));
                txtNotelp.setText((String) model.getValueAt(i,4));
                jenkil = ((String) model.getValueAt(i,5));
                if (jenkil.equals("Laki Laki")){
                    rbLaki.setSelected(true);
                }else if (jenkil.equals("Perempuan")){
                    rbPerempuan.setSelected(true);
                }
                txtUsername.setText((String) model.getValueAt(i,6));
                txtPassword.setText((String) model.getValueAt(i,7));
                radiobutton();
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
                            String query = "EXEC sp_DeleteKaryawan @Id_Karyawan=?";
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
                reset();
            }
        });
        btnCari.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                model.getDataVector().removeAllElements(); //menghapus semua data ditamp
                model.fireTableDataChanged(); // memberitahu data telah ksong

                try{
                    DBConnect connection = new DBConnect();
                    connection.stat = connection.conn.createStatement();
                    String query = "SELECT * FROM Karyawan WHERE Id_Karyawan = '" + txtCari.getText() + "' AND Status = 1";
                    connection.result = connection.stat.executeQuery(query);

                    //lakukan baris perbari data
                    while (connection.result.next()) {
                        Object[] obj = new Object[8];
                        obj[0] = connection.result.getString(1);
                        obj[1] = connection.result.getString(2);
                        obj[2] = connection.result.getString(3);
                        obj[3] = connection.result.getString(4);
                        obj[4] = connection.result.getString(5);
                        obj[5] = connection.result.getString(6);
                        obj[6] = connection.result.getString(7);
                        obj[7] = connection.result.getString(8);
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
                else if (txtNotelp.getText().length() >= 13) e.consume();
            }
        });
    }

    public void radiobutton(){
        if (rbLaki.isSelected()){
            rbPerempuan.setSelected(false);
            rbPerempuan.enable(false);
        }else{
            rbLaki.setSelected(false);
            rbLaki.enable(false);
        }
    }
    public void autokode(){
        try{
            String sql = "SELECT * FROM Karyawan ORDER BY Id_Karyawan desc";
            connection.stat = connection.conn.createStatement();
            connection.result = connection.stat.executeQuery(sql);
            if (connection.result.next()) {
                autokode = connection.result.getString("Id_Karyawan").substring(4);
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
                txtID.setText("KR" + nol + AN);
                txtID.setEnabled(false);

            }else {
                txtID.setText("JK0001");
                txtID.setEnabled(false);
            }
            connection.stat.close();
            connection.result.close();
        }catch (Exception e1){
            System.out.println("Terjadi error pada CRUD: " + e1);
        }
    }
    public void FrameRadioButton(){
        ButtonGroup grup = new ButtonGroup();
        grup.add(rbLaki);
        grup.add(rbPerempuan);
    }
    public void tampilJabatan() {
        try {
            DBConnect connection = new DBConnect();
            connection.stat = connection.conn.createStatement();
            String sql1 = "SELECT Nama_Jabatan FROM Jabatan_Karyawan WHERE Status = 1";
            connection.result = connection.stat.executeQuery(sql1);

            while (connection.result.next()) {
                cbJabatan.addItem(connection.result.getString("Nama_Jabatan"));
            }
            connection.stat.close();
            connection.result.close();
        } catch (SQLException ex) {
            System.out.println("Terjadi error saat load data ID Jabatan" + ex);
        }
    }

    public void tampilIdJabatan() {
        try {
            DBConnect connection = new DBConnect();
            connection.stat = connection.conn.createStatement();
            String sql1 = "SELECT Id_Jabatan FROM Jabatan_Karyawan WHERE Nama_Jabatan LIKE '%" +
                    cbJabatan.getSelectedItem() + "%'";
            connection.result = connection.stat.executeQuery(sql1);

            while (connection.result.next()) {
                jabatan = (connection.result.getString("Id_Jabatan"));
            }
            connection.stat.close();
            connection.result.close();
        } catch (SQLException ex) {
            System.out.println("Terjadi error saat load data" + ex);
        }
    }

    public void LoadIdJabatan(){
        try {
            DBConnect connection = new DBConnect();
            connection.stat = connection.conn.createStatement();
            String sql1 = "SELECT Nama_Jabatan FROM Jabatan_Karyawan WHERE Id_Jabatan LIKE '%" +
                    jabatan + "%'";
            connection.result = connection.stat.executeQuery(sql1);

            while (connection.result.next()) {
                String jabatan = connection.result.getString("Nama_Jabatan");
                cbJabatan.setSelectedItem(jabatan);
            }
            connection.stat.close();
            connection.result.close();
        }catch (SQLException ex){
            System.out.println("Terjadi error saat load data  " +ex);
        }
    }

    public void loadData(){
        //menghapus seluruh data ditampilkan(jika ada) untuk tampilan pertama
        model.getDataVector().removeAllElements();
        //memberi tahu data yang kosong
        model.fireTableDataChanged();
        try{
            connection.stat = connection.conn.createStatement();
            String query = "EXEC sp_LoadKaryawan";
            connection.result = connection.stat.executeQuery(query);
            //lakukan baris perbaris
            while(connection.result.next()){
                String TempStatus = "";
                Object[] obj = new Object[8];
                obj[0] = connection.result.getString(1);
                obj[1] = connection.result.getString(2);
                obj[2] = connection.result.getString(3);
                obj[3] = connection.result.getString(4);
                obj[4] = connection.result.getString(5);
                obj[5] = connection.result.getString(6);
                obj[6] = connection.result.getString(7);
                obj[7] = connection.result.getString(8);
                model.addRow(obj);
            }
            connection.stat.close();
            connection.result.close();
        }
        catch (Exception e){
            System.out.println("Terjadi error saat load data member: "+e);
        }
    }

    public void addColomn(){
        model.addColumn("ID Pegawai");
        model.addColumn("Jabatan");
        model.addColumn("Nama Karyawan");
        model.addColumn("Email");
        model.addColumn("Nomor Telepon");
        model.addColumn("Jenis Kelamin");
        model.addColumn("Username");
        model.addColumn("Password");
    }

    void validasiHuruf(KeyEvent b){
        if(Character.isDigit(b.getKeyChar())){
            b.consume();
        }
    }

    public void clear(){
        txtNama.setText("");
        cbJabatan.setSelectedItem(null);
        txtEmail.setText("");
        txtNotelp.setText("");
        rbLaki.setSelected(false);
        rbPerempuan.setSelected(false);
        txtUsername.setText("");
        txtPassword.setText("");
    }
    public void reset() {
        clear();
        loadData();
        autokode();
    }
}
