package DataMaster;

import DBConnect.DBConnect;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.*;
import java.sql.SQLException;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;
import java.text.DecimalFormat;

public class MasterHandphone extends JFrame{
    public JPanel JPHandphone;
    private JPanel JPanel2;
    private javax.swing.JPanel JPanel;
    private JTextField txtID;
    private JTextField txtHarga;
    private JTextField txtStokHandphone;
    private JComboBox cbMerk;
    private JTable tableView;
    private JButton btnSimpan;
    private JButton btnUbah;
    private JButton btnHapus;
    private JButton btnBatal;
    private JTextField txtCari;
    private JButton btnCari;
    private JTextField txtNamaHandphone;
    private JScrollPane tableview;

    DBConnect connection = new DBConnect();
    private DefaultTableModel model = new DefaultTableModel();

    //variable
    int status;
    String id, merk, nama, autokode, stok,harga;

    public MasterHandphone(){
        tampilMerk();
        model = new DefaultTableModel();
        tableView.setModel(model);
        setContentPane(JPHandphone);
        addColomn();
        autokode();
        loadData();
        cbMerk.setSelectedItem(null);

        btnSimpan.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                id = txtID.getText();
                nama = txtNamaHandphone.getText();
                harga = txtHarga.getText();
                String nilai_ribuan = harga ;
                harga = nilai_ribuan.replace(",", "");
                stok = txtStokHandphone.getText();
                status = 1;
                Boolean found = false;

                //Validasi Jika Memasukkan Nama Yang sama
                Object[] obj = new Object[2];
                obj[0] = id;
                obj[1] = txtNamaHandphone.getText();
                int j = tableView.getModel().getRowCount();
                for (int k = 0; k < j; k++) {
                    if(obj[1].equals(model.getValueAt(k,2)))
                    {
                        found = true;
                    }
                }
                if(found) {
                    JOptionPane.showMessageDialog(null, "Deskripsi Handphone Sudah Ada!", "Peringatan",
                            JOptionPane.WARNING_MESSAGE);
                    txtNamaHandphone.setText("");
                }else{
                    try {
                        if (nama.equals("") | harga.equals("") | cbMerk.equals("") | stok.equals(""))
                        {
                            throw new Exception("Mohon untuk mengisi seluruh data!");
                        } else {
                            try {
                                String.valueOf(cbMerk.getSelectedItem());
                                tampilIdMerk();
                                String query = "EXEC sp_InsertHandphone @Id_Handphone=?, @Id_Merk=?, @Nama_Handphone=?, @Stok_Handphone=?, @Harga_Handphone=?";
                                connection.pstat = connection.conn.prepareStatement(query);
                                connection.pstat.setString(1, id);
                                connection.pstat.setString(2, merk);
                                connection.pstat.setString(3, nama);
                                connection.pstat.setString(4, stok);
                                connection.pstat.setString(5, harga);
                                connection.pstat.executeUpdate();
                                connection.pstat.close();
                                JOptionPane.showMessageDialog(null, "Insert data berhasil");
                                reset();
                            } catch (Exception ex) {
                                System.out.println("Error saat inssert data ke database" + ex);
                            }
                        }
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(null, ex.getMessage());
                    }
                }
            }
        });

        btnUbah.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                id = txtID.getText();
                nama = txtNamaHandphone.getText();
                harga = txtHarga.getText();
                String nilai_ribuan = harga ;
                harga = nilai_ribuan.replace(",", "");
                stok = txtStokHandphone.getText();

                try {
                    if (nama.equals("") | harga.equals("") | cbMerk.equals("") | stok.equals(""))
                    {
                        throw new Exception("Mohon untuk mengisi seluruh data!");
                    } else {
                        try {
                            String.valueOf(cbMerk.getSelectedItem());
                            tampilIdMerk();
                            String query = "EXEC sp_UpdateHandphone @Id_Merk=?, @Nama_Handphone=?, @Stok_Handphone=?, @Harga_Handphone=? ,@Id_Handphone=?";
                            connection.pstat = connection.conn.prepareStatement(query);
                            connection.pstat.setString(5, id);
                            connection.pstat.setString(1, merk);
                            connection.pstat.setString(2, nama);
                            connection.pstat.setString(3, stok);
                            connection.pstat.setString(4, harga);
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


        btnCari.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                model.getDataVector().removeAllElements(); //menghapus semua data ditampilkan
                model.fireTableDataChanged(); // memberitahu data telah ksong

                try {
                    DBConnect connection = new DBConnect();
                    connection.stat = connection.conn.createStatement();
                    String query = "SELECT * FROM Handphone WHERE Id_Handphone = '" + txtCari.getText() + "' AND Status = 1";
                    connection.result = connection.stat.executeQuery(query);

                    //lakukan baris perbari data
                    while (connection.result.next()) {
                        Object[] obj = new Object[2];
                        obj[0] = connection.result.getString(1);
                        obj[1] = connection.result.getString(2);
                        model.addRow(obj);
                    }
                    if (model.getRowCount() == 0) {
                        JOptionPane.showMessageDialog(null, "Data tidak ditemukan");
                    }
                    connection.stat.close();
                    connection.result.close();
                } catch (Exception ex) {
                    System.out.println("Error saat pencarian data" + e);
                }
            }
        });

        btnBatal.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cbMerk.setSelectedItem(null);
                txtCari.setText("");
                reset();
            }
        });

        btnHapus.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int opsi;
                id = txtID.getText();
                if (txtNamaHandphone.getText().equals("")) {
                    JOptionPane.showMessageDialog(null, "Input data yang ingin dihapus!", "Peringatan",
                            JOptionPane.WARNING_MESSAGE);
                } else {
                    try {
                        opsi = JOptionPane.showConfirmDialog(null, "Apakah ingin menghapus data?",
                                "Konfirmasi", JOptionPane.YES_OPTION, JOptionPane.WARNING_MESSAGE);
                        if (opsi != 0) {
                            JOptionPane.showMessageDialog(null, "Data Tidak Berhasil Dihapus");
                        } else {
                            String query = "EXEC sp_DeleteHandphone @Id_Handphone =?";
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
        tableView.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                int i = tableView.getSelectedRow();
                txtID.setText((String) model.getValueAt(i, 0));
                cbMerk.setSelectedItem((String) model.getValueAt(i, 1));
                txtNamaHandphone.setText((String) model.getValueAt(i, 2));
                txtStokHandphone.setText(String.valueOf((Integer) model.getValueAt(i, 3)));
                txtHarga.setText((String) model.getValueAt(i, 4));
            }
        });
        txtStokHandphone.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                super.keyTyped(e);
                char stok = e.getKeyChar();
                if (!(Character.isDigit(stok))) e.consume();
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

    private String formatrupiah(int value){
        DecimalFormat formater = new DecimalFormat("#,###,###");
        DecimalFormatSymbols symbol = formater.getDecimalFormatSymbols();
        symbol.setMonetaryDecimalSeparator(',');
        symbol.setGroupingSeparator(',');
        formater.setDecimalFormatSymbols(symbol);
        return  formater.format(value);
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

    public void autokode(){
        try{
            String sql = "SELECT * FROM Handphone ORDER BY Id_Handphone desc";
            connection.stat = connection.conn.createStatement();
            connection.result = connection.stat.executeQuery(sql);
            if (connection.result.next()) {
                autokode = connection.result.getString("Id_Handphone").substring(4);
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
                txtID.setText("HP" + nol + AN);
                txtID.setEnabled(false);

            }else {
                txtID.setText("HP0001");
                txtID.setEnabled(false);
            }
            connection.stat.close();
            connection.result.close();
        }catch (Exception e1){
            System.out.println("Terjadi error pada CRUD: " + e1);
        }
    }

    public String convertRupiah(double dbPrice) {
        Locale localId = new Locale("in", "ID");
        NumberFormat formatter = NumberFormat.getCurrencyInstance(localId);
        String strFormat = formatter.format(dbPrice);
        return strFormat;
    }

    public double RptoDouble(String Price) throws ParseException {
        Locale myIndonesianLocale = new Locale("in", "ID");
        Number number = NumberFormat.getCurrencyInstance(myIndonesianLocale).parse(Price);
        double nilai = number.doubleValue();
        String temp = String.format("%.2f", nilai);
        double dblFormat = Double.parseDouble(temp);
        return dblFormat;
    }

    public void loadData(){
        model.getDataVector().removeAllElements();
        model.fireTableDataChanged();
        try{
            DBConnect connection = new DBConnect();
            connection.stat = connection.conn.createStatement();
            String query = "EXEC sp_LoadHandphone";
            connection.result = connection.stat.executeQuery(query);
            while (connection.result.next()){
                Object[] obj = new Object[5];
                obj[0] = connection.result.getString(1);
                obj[1] = connection.result.getString(2);
                obj[2] = connection.result.getString(3);
                obj[3] = connection.result.getInt(4);
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
        model.addColumn("ID Handphone");
        model.addColumn("Merk Handphone");
        model.addColumn("Nama Handphone");
        model.addColumn("Stok Handphone");
        model.addColumn("Harga Handphone");
    }

    public void clear(){
        cbMerk.setSelectedItem("");
        txtNamaHandphone.setText("");
        txtStokHandphone.setText("");
        txtHarga.setText("");
    }
    public void reset() {
        clear();
        loadData();
        autokode();
    }
}
