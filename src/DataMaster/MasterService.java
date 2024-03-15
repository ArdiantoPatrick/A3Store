package DataMaster;

import DBConnect.DBConnect;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.*;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

public class MasterService {
    public JPanel JPService;
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
    private JComboBox cbMerk;
    private JComboBox cbJenis;
    private JTextField txtNama;
    private JTextField txtHarga;

    DBConnect connection = new DBConnect();
    private DefaultTableModel model = new DefaultTableModel();

    //variabel
    String id,merk,jenis,nama,harga,autokode;
    float harga2;

    public MasterService(){
        tampilJenis();
        tampilMerk();
        model = new DefaultTableModel();
        tableView.setModel(model);
        addColomn();
        loadData();
        clear();
        autokode();

        btnSimpan.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                id = txtID.getText();
                nama = txtNama.getText();
                harga = txtHarga.getText();
                String nilai_ribuan = harga ;
                harga = nilai_ribuan.replace(",", "");
                try {
                    Boolean found = false;

                    //Validasi Jika Memasukkan Nama Yang sama
                    Object[] obj = new Object[2];
                    obj[0] = id;
                    obj[1] = txtNama.getText();
                    int j = tableView.getModel().getRowCount();
                    for (int k = 0; k < j; k++) {
                        if(obj[1].equals(model.getValueAt(k,3)))
                        {
                            found = true;
                        }
                    }
                    if(found) {
                        JOptionPane.showMessageDialog(null, "Deskripsi Service Sudah Ada!", "Peringatan",
                                JOptionPane.WARNING_MESSAGE);
                        txtNama.setText("");
                    }
                    else {
                        try {
                            if (nama.equals("") | harga.equals("") | cbMerk.equals("") | cbJenis.equals(""))
                            {
                                throw new Exception("Mohon untuk mengisi seluruh data!");
                            }else {
                                String.valueOf(cbJenis.getSelectedItem());
                                tampilIdJenis();
                                String.valueOf(cbMerk.getSelectedItem());
                                tampilIdMerk();
                                String query = "EXEC sp_InsertService @Id_Service=?, @Id_Jenis_Service=?, @Id_Merk=?, @Nama_Service=?, @Harga_Service=?";
                                connection.pstat = connection.conn.prepareStatement(query);
                                connection.pstat.setString(1, id);
                                connection.pstat.setString(2, jenis);
                                connection.pstat.setString(3, merk);
                                connection.pstat.setString(4, nama);
                                connection.pstat.setString(5, harga);
                                connection.pstat.executeUpdate();
                                connection.pstat.close();
                                JOptionPane.showMessageDialog(null, "Insert data berhasil");
                                clear();
                                reset();
                                autokode();
                            }
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
                id = txtID.getText();
                nama = txtNama.getText();
                harga = txtHarga.getText();
                String nilai_ribuan = harga ;
                harga = nilai_ribuan.replace(",", "");
                try {
                    if (nama.equals("") | harga.equals("") | cbMerk.equals("") | cbJenis.equals("")) {
                        throw new Exception("Mohon untuk mengisi seluruh data!");
                    } else {
                        try {
                            String.valueOf(cbJenis.getSelectedItem());
                            tampilIdJenis();
                            String.valueOf(cbMerk.getSelectedItem());
                            tampilIdMerk();
                            String query = "EXEC sp_UpdateService @Id_Service=?, @Id_Jenis_Service=?, @Id_Merk=?, @Nama_Service=? ,@Harga_Service=?";
                            connection.pstat = connection.conn.prepareStatement(query);
                            connection.pstat.setString(1, id);
                            connection.pstat.setString(2, jenis);
                            connection.pstat.setString(3, merk);
                            connection.pstat.setString(4, nama);
                            connection.pstat.setString(5, harga);
                            connection.pstat.executeUpdate();
                            connection.pstat.close();
                            JOptionPane.showMessageDialog(null, "Data Berhasil Diubah");
                            clear();
                            loadData();
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
                cbJenis.setSelectedItem((String) model.getValueAt(i,2));
                cbMerk.setSelectedItem((String) model.getValueAt(i,1));
                txtNama.setText((String) model.getValueAt(i,3));
                txtHarga.setText((String) model.getValueAt(i,4));
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
                            String query = "EXEC sp_DeleteService @Id_Service=?";
                            connection.pstat = connection.conn.prepareStatement(query);
                            connection.pstat.setString(1, id);
                            connection.pstat.executeUpdate();
                            connection.pstat.close();
                            JOptionPane.showMessageDialog(null, "Data Berhasil Dihapus");
                        }
                    } catch (Exception ex) {
                        System.out.println("Error pada saat delete data" + ex);
                    }
                    clear();
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
                    String query = "EXEC sp_CariService @Id_Service=?";
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
        txtHarga.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                super.keyTyped(e);
                char harga = e.getKeyChar();
                if (!(Character.isDigit(harga))) e.consume();
            }
        });
        txtHarga.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                super.keyReleased(e);
                try {
                    String hrga = txtHarga.getText().replaceAll("\\,","");
                    double dblharga = Double.parseDouble(hrga);
                    DecimalFormat df = new DecimalFormat("#,###,###");
                    if (dblharga > 999){
                        txtHarga.setText(df.format(dblharga));
                    }
                }catch (Exception ex){

                }
            }
        });
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
            System.out.println("Terjadi error saat load data ID Jabatan" + ex);
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

    public void tampilJenis() {
        try {
            DBConnect connection = new DBConnect();
            connection.stat = connection.conn.createStatement();
            String sql1 = "SELECT Nama_Jenis_Service FROM Jenis_Service WHERE Status = 1";
            connection.result = connection.stat.executeQuery(sql1);

            while (connection.result.next()) {
                cbJenis.addItem(connection.result.getString("Nama_Jenis_Service"));
            }
            connection.stat.close();
            connection.result.close();
        } catch (SQLException ex) {
            System.out.println("Terjadi error saat load data ID Jabatan" + ex);
        }
    }

    public void tampilIdJenis() {
        try {
            DBConnect connection = new DBConnect();
            connection.stat = connection.conn.createStatement();
            String sql1 = "SELECT Id_Jenis_Service FROM Jenis_Service WHERE Nama_Jenis_Service LIKE '%" +
                    String.valueOf(cbJenis.getSelectedItem()) + "%'";
            connection.result = connection.stat.executeQuery(sql1);

            while (connection.result.next()) {
                jenis = (connection.result.getString("Id_Jenis_Service"));
            }
            connection.stat.close();
            connection.result.close();
        } catch (SQLException ex) {
            System.out.println("Terjadi error saat load data" + ex);
        }
    }
    public void autokode(){
        try{
            String sql = "SELECT * FROM Service ORDER BY Id_Service desc";
            connection.stat = connection.conn.createStatement();
            connection.result = connection.stat.executeQuery(sql);
            if (connection.result.next()) {
                autokode = connection.result.getString("Id_Service").substring(4);
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
                txtID.setText("SR" + nol + AN);
                txtID.setEnabled(false);

            }else {
                txtID.setText("SR0001");
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
            String query = "EXEC sp_LoadService";
            connection.result = connection.stat.executeQuery(query);
            while (connection.result.next()){
                Object[] obj = new Object[5];
                obj[0] = connection.result.getString(1);
                obj[1] = connection.result.getString(2);
                obj[2] = connection.result.getString(3);
                obj[3] = connection.result.getString(4);
                obj[4] = formatrupiah(connection.result.getInt(5));
                model.addRow(obj);
            }
            connection.stat.close();
            connection.result.close();
        }catch (Exception ex){
            System.out.println("Terjadi error saat load data" +ex);
        }
    }

    public void addColomn(){
        model.addColumn("ID Service");
        model.addColumn("Nama Merk");
        model.addColumn("Jenis Service");
        model.addColumn("Nama Service");
        model.addColumn("Harga Service");
    }

    public void clear(){
        cbJenis.setSelectedItem(null);
        cbMerk.setSelectedItem(null);
        txtNama.setText("");
        txtHarga.setText("");
    }

    private String formatrupiah(int value){
        DecimalFormat formater = new DecimalFormat("#,###,###");
        DecimalFormatSymbols symbol = formater.getDecimalFormatSymbols();
        symbol.setMonetaryDecimalSeparator(',');
        symbol.setGroupingSeparator(',');
        formater.setDecimalFormatSymbols(symbol);
        return  formater.format(value);
    }

    public void reset() {
        clear();
        loadData();
        autokode();
    }
}
