package DataTransaksi;

import DBConnect.DBConnect;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;

public class TransaksiSupplier {
    public JPanel TransaksiSupplier;
    private JTextField tbBarang;
    private JTextField tbHarga;
    private JTextField tbKuantitas;
    private JTable Keranjang;
    private JTable TableHandphone;
    private JTable TableAksesoris;
    private JButton btnTambah;
    private JButton btnBatal;
    private JComboBox cbSupplier;
    private JButton btnSimpan;
    private JButton btnBatalsemua;
    private JTextField tbSubTotal;
    private JTextField tbMerk;
    private JLabel lblGrandTotal;
    private JButton btnSelesai;
    private JTextField tbNamaBarang;
    private JComboBox cbMerk;
    private JTextField tbStok;
    private JTextField tbHargaAkhir;
    private JTextField tbTotal;

    String merk,idbarang,id,idtr,idkaryawan,kode,idsupplier,jumlah;
    double totalharga;
    DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    Date currentDate = new Date();

    DBConnect connection = new DBConnect();
    private DefaultTableModel model = new DefaultTableModel();
    private DefaultTableModel model2 = new DefaultTableModel();
    private DefaultTableModel model3 = new DefaultTableModel();

    public TransaksiSupplier(String value) {
        autokode();
        model = new DefaultTableModel();
        Keranjang.setModel(model);
        addColomnKeranjang();

        model2 = new DefaultTableModel();
        TableHandphone.setModel(model2);
        addColomnHandphone();

        model3 = new DefaultTableModel();
        TableAksesoris.setModel(model3);
        addColomnAksesoris();

        loadDataAksesoris();
        loadDataHandphone();
        tampilMerk();
        cbMerk.setSelectedItem(null);

        btnBatal.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clear();
                cbMerk.setSelectedItem(null);
                cbSupplier.setSelectedItem(null);
            }
        });

        btnTambah.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String harga = tbHarga.getText();
                String nilai_ribuan = harga;
                harga = nilai_ribuan.replace(",", "");

                String harga2 = tbHargaAkhir.getText();
                String nilai_ribuan2 = harga2;
                harga2 = nilai_ribuan2.replace(",", "");

                Boolean found = false;
                Object[] obj = new Object[6];
                obj[0] = idbarang;
                obj[1] = cbSupplier.getSelectedItem();
                obj[2] = tbNamaBarang.getText();
                obj[3] = harga;
                obj[4] = tbKuantitas.getText();
                obj[5] = harga2;

                int j = Keranjang.getModel().getRowCount();
                for (int k = 0; k < j; k++) {
                    //menghitung nilai harga*jumlah setiap baris
                    if (obj[0].equals(model.getValueAt(k, 0))) {
                        found = true;
                    }
                }
                if (found) {
                    JOptionPane.showMessageDialog(null, "Data sudah pernah ditambahkan!", "Peringatan",
                            JOptionPane.WARNING_MESSAGE);
                    tbNamaBarang.setText(null);
                    tbKuantitas.setText(null);
                    tbHarga.setText(null);
                    tbHargaAkhir.setText(null);
                    tbStok.setText(null);
                } else if(tbNamaBarang.getText().equals("")){
                    JOptionPane.showMessageDialog(null, "Pilih barang yang ingin dibeli !", "Peringatan",
                            JOptionPane.WARNING_MESSAGE);
                } else if (tbKuantitas.getText().equals("")) {
                    JOptionPane.showMessageDialog(null, "Isikan Kuantitas Barang!", "Peringatan",
                            JOptionPane.WARNING_MESSAGE);
                } else if (tbKuantitas.getText().equals("0")) {
                    JOptionPane.showMessageDialog(null, "Minimal pembelian adalah 1 buah !", "Peringatan",
                            JOptionPane.WARNING_MESSAGE);
                }else if (cbSupplier.getSelectedItem() == null) {
                    JOptionPane.showMessageDialog(null, "Pilih Supplier!", "Peringatan",
                            JOptionPane.WARNING_MESSAGE);
                }else {
                    idsupplier = cbSupplier.getSelectedItem().toString();
                    model.addRow(obj);
                    totalharga = 0;
                    int row = model.getRowCount();
                    for (int i = 0; i < row; i++) {
                        String temp = (String) model.getValueAt(i, 5);
                        double HT1 = Double.parseDouble(temp);
                        totalharga = HT1 + totalharga;
                    }
                    lblGrandTotal.setText(convertRupiah(totalharga));
                    clear();
                    cbMerk.setEnabled(false);
                    cbSupplier.setEnabled(false);
                }
            }
        });

        btnBatalsemua.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clear();
                cbMerk.setEnabled(true);
                cbSupplier.setEnabled(true);
                lblGrandTotal.setText("0");
                cbMerk.setSelectedItem(null);
                cbSupplier.removeAllItems();
                DefaultTableModel model = (DefaultTableModel) Keranjang.getModel();
                model.setRowCount(0);
                DefaultTableModel model2 = (DefaultTableModel) TableHandphone.getModel();
                model2.setRowCount(0);
                DefaultTableModel model3 = (DefaultTableModel) TableAksesoris.getModel();
                model3.setRowCount(0);
                JOptionPane.showMessageDialog(null, "Transaksi Dibatalkan !", "Peringatan",
                        JOptionPane.WARNING_MESSAGE);
            }
        });
        btnSimpan.addActionListener(new  ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                autokode();
                tampilIdSupplier();
                int row = model.getRowCount();
                int k = 0;
                id = kode;
                idkaryawan = value;
                try {
                    totalharga = RptoDouble(lblGrandTotal.getText());
                } catch (ParseException ex) {
                    ex.printStackTrace();
                }

                try {
                    String.valueOf(cbSupplier.getSelectedItem());
                    String query = "EXEC sp_InputTransaksiSupplier @Id_TrSupplier=?, @Id_Karyawan=?, @Id_Supplier=?, @GrandTotal=?, @Tgl_Pembelian=?";
                        connection.pstat = connection.conn.prepareStatement(query);
                        connection.pstat.setString(1, id);
                        connection.pstat.setString(2, idkaryawan);
                        connection.pstat.setString(3, idsupplier);
                        connection.pstat.setDouble(4, totalharga);
                        connection.pstat.setDate(5, java.sql.Date.valueOf((sdf.format(new Date()))));
                        connection.pstat.executeUpdate();

                    for(int i = 0; i<row;i++){
                        idbarang = (String) model.getValueAt(i, 0);
                        jumlah = (String) model.getValueAt(i, 4);
                        String sql2 = "EXEC sp_DetailSupplier @Id_TrSupplier=?,@Id_Handphone=?, @Id_Aksesoris=?, @Jumlah=?";
                        connection.pstat = connection.conn.prepareStatement(sql2);
                        connection.pstat.setString(1, id);
                        connection.pstat.setString(2, idbarang);
                        connection.pstat.setString(3, idbarang);
                        connection.pstat.setString(4, jumlah);
                        connection.pstat.executeUpdate();
                        connection.pstat.close();
                    }
                    JOptionPane.showMessageDialog(null, "Data berhasil disimpan dengan ID " + kode, "Transaksi Penjualan",
                            JOptionPane.INFORMATION_MESSAGE);
                    clear();
                    autokode();
                    DefaultTableModel model = (DefaultTableModel) Keranjang.getModel();
                    model.setRowCount(0);
                    DefaultTableModel model2 = (DefaultTableModel) TableHandphone.getModel();
                    model2.setRowCount(0);
                    DefaultTableModel model3 = (DefaultTableModel) TableAksesoris.getModel();
                    model3.setRowCount(0);
                    cbMerk.setEnabled(true);
                    cbSupplier.setEnabled(true);
                    cbSupplier.removeAllItems();

                } catch (Exception ex) {
                    System.out.println("Error saat insert data ke database" + ex);
                }
            }
        });
        tbKuantitas.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if(e.getKeyCode() == KeyEvent.VK_ENTER) {
                    Integer qty = Integer.parseInt(tbKuantitas.getText());
                    String harga2 = tbHarga.getText();
                    String nilai_ribuan = harga2;
                    harga2 = nilai_ribuan.replace(",", "");
                    Integer harga = Integer.parseInt(harga2);
                    Integer tempharga = qty * harga;
                    tbHargaAkhir.setText(String.valueOf(formatrupiah(tempharga)));
                }
            }
        });
        cbMerk.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadDataAksesoris();
                loadDataHandphone();
                tampilSupplier();
                cbSupplier.setSelectedItem(null);
            }
        });
        TableHandphone.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                int harga;
                int i = TableHandphone.getSelectedRow();

                String harga2 = ((String) model2.getValueAt(i, 3));
                String nilai_ribuan = harga2;
                harga2 = nilai_ribuan.replace(",", "");
                harga = Integer.parseInt(harga2);
                harga = harga - (harga * 20/100);
                tbHarga.setText(String.valueOf(formatrupiah(harga)));

                idbarang = (String) model2.getValueAt(i, 0);
                tbNamaBarang.setText((String) model2.getValueAt(i, 1));
                tbStok.setText(String.valueOf((Integer) model2.getValueAt(i, 4)));
            }
        });
        TableAksesoris.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                int harga;
                int i = TableAksesoris.getSelectedRow();

                String harga2 = ((String) model3.getValueAt(i, 3));
                String nilai_ribuan = harga2;
                harga2 = nilai_ribuan.replace(",", "");
                harga = Integer.parseInt(harga2);
                harga = harga - (harga * 20/100);
                tbHarga.setText(String.valueOf(formatrupiah(harga)));

                idbarang = (String) model3.getValueAt(i, 0);
                tbNamaBarang.setText((String) model3.getValueAt(i, 1));
                tbStok.setText(String.valueOf((Integer) model3.getValueAt(i, 4)));
            }
        });
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

    private String formatrupiah(int value){
        DecimalFormat formater = new DecimalFormat("#,###,###");
        DecimalFormatSymbols symbol = formater.getDecimalFormatSymbols();
        symbol.setMonetaryDecimalSeparator(',');
        symbol.setGroupingSeparator(',');
        formater.setDecimalFormatSymbols(symbol);
        return  formater.format(value);
    }

    public void clear(){
        tbStok.setText("");
        tbNamaBarang.setText("");
        tbKuantitas.setText("");
        tbHarga.setText("");
        tbHargaAkhir.setText("");
    }
    public void addColomnKeranjang() {
        model.addColumn("Id Barang");
        model.addColumn("Nama Supplier");
        model.addColumn("Nama Barang");
        model.addColumn("Harga");
        model.addColumn("Kuantitas");
        model.addColumn("Total Harga");
    }

    public void addColomnHandphone () {
        model2.addColumn("Id Handphone");
        model2.addColumn("Nama Handphone");
        model2.addColumn("Merk");
        model2.addColumn("Harga Handphone");
        model2.addColumn("Stok Handphone");
    }

    public void addColomnAksesoris () {
        model3.addColumn("Id Aksesoris");
        model3.addColumn("Nama Aksesoris");
        model3.addColumn("Merk");
        model3.addColumn("Harga Aksesoris");
        model3.addColumn("Stok Aksesoris");
    }

    public void autokode() {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyMMdd");
            String sql = "SELECT * FROM Transaksi_Supplier ORDER BY Id_TrSupplier desc";
            connection.stat = connection.conn.createStatement();
            connection.result = connection.stat.executeQuery(sql);
            if (connection.result.next()) {
                kode = connection.result.getString("Id_TrSupplier").substring(4);
                String AN = "" + (Integer.parseInt(kode) + 1);
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
                kode = "TS" + nol + AN;

            } else {
                kode = "TS"+ "0001";
            }
        } catch (Exception e1) {
            System.out.println("Terjadi error pada autokode2: " + e1);
        }
    }

    public void loadDataAksesoris() {
        model3.getDataVector().removeAllElements();
        model3.fireTableDataChanged();
        PreparedStatement st;
        try {
            tampilIdMerk();
            DBConnect connection = new DBConnect();
            connection.stat = connection.conn.createStatement();
            st = connection.conn.prepareStatement("EXEC sp_LoadAksesorisTrS @Id_Merk=?");
            st.setString(1,merk);
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                Object[] obj = new Object[5];
                obj[0] = rs.getString(1);
                obj[1] = rs.getString(2);
                obj[2] = rs.getString(3);
                obj[3] = formatrupiah(rs.getInt(4));
                obj[4] = rs.getInt(5);
                model3.addRow(obj);
            }
        } catch (Exception ex) {
            System.out.println("Terjadi error saat load data" + ex);
        }
    }

    public void loadDataHandphone() {
        model2.getDataVector().removeAllElements();
        model2.fireTableDataChanged();
        PreparedStatement st;
        try {
            tampilIdMerk();
            DBConnect connection = new DBConnect();
            connection.stat = connection.conn.createStatement();
            st = connection.conn.prepareStatement("EXEC sp_LoadHandphoneTrS @Id_Merk=?");
            st.setString(1,merk);
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                Object[] obj = new Object[5];
                obj[0] = rs.getString(1);
                obj[1] = rs.getString(2);
                obj[2] = rs.getString(3);
                obj[3] = formatrupiah(rs.getInt(4));
                obj[4] = rs.getInt(5);
                model2.addRow(obj);
            }
        } catch (Exception ex) {
            System.out.println("Terjadi error saat load data" + ex);
        }
    }

    public void tampilSupplier() {
        PreparedStatement st;
        try {
            tampilIdMerk();
            DBConnect connection = new DBConnect();
            connection.stat = connection.conn.createStatement();
            st = connection.conn.prepareStatement("sp_LoadSupplierTrS @Id_Merk=?");
            st.setString(1,merk);
            ResultSet rs = st.executeQuery();
            cbSupplier.removeAllItems();
            while (rs.next()) {
                cbSupplier.addItem(rs.getString("Nama_Supplier"));
            }
        } catch (SQLException ex) {
            System.out.println("Terjadi error saat load data ID Supplier" + ex);
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
                    cbMerk.getSelectedItem() + "%'";
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

    public void tampilIdSupplier() {
        int j = Keranjang.getModel().getRowCount();
        int k = 0;
        String namasupplier = (String) model.getValueAt(k, 0);
        try {
            DBConnect connection = new DBConnect();
            connection.stat = connection.conn.createStatement();
            String sql1 = "SELECT Id_Supplier FROM Supplier WHERE Nama_Supplier LIKE '%" +
                    idsupplier + "%'";
            connection.result = connection.stat.executeQuery(sql1);

            while (connection.result.next()) {
                idsupplier = (connection.result.getString("Id_Supplier"));
            }
            connection.stat.close();
            connection.result.close();
        } catch (SQLException ex) {
            System.out.println("Terjadi error saat load data" + ex);
        }
    }
}
