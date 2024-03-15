package DataTransaksi;

import DBConnect.DBConnect;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.*;
import java.awt.print.PrinterException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;

public class TransaksiPenjualan {
    private JTable TableHandphone;
    private JTable Keranjang;
    private JTable TableAksesoris;
    public JPanel TransaksiPenjualan;
    private JPanel PanelKiriAtas;
    private JPanel PanelKanan;
    private JPanel PanelKiriBawah;
    private JButton btnTambah;
    private JButton btnHapus;
    private JButton btnBayar;
    private JComboBox cbCustomer;
    private JComboBox cbMerk;
    private JTextField txtStok;
    private JTextField txtNamabarang;
    private JTextField txtHargabarang;
    private JTextField txtHargaakhir;
    private JTextField txtkuantitas;
    private JLabel lblTotalHarga;
    private JTextField txtUang;
    private JTextField txtKembali;
    private JButton btnBatal;
    private JButton btnSImpan;
    private JTextField txtCari;
    private JButton btnPrint;
    private JTextArea tbNota;

    String merk,idbarang,namabarang,harga,kuantitas,idkaryawan,idtr,kode,idcustomer,jumlah,harga2;
    double totalharga;
    DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    DBConnect connection = new DBConnect();
    private DefaultTableModel model = new DefaultTableModel();
    private DefaultTableModel model2 = new DefaultTableModel();
    private DefaultTableModel model3 = new DefaultTableModel();

    public TransaksiPenjualan(String value) {
        tbNota.setVisible(false);
        btnPrint.setVisible(false);
        autokode();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyMMdd");
        model = new DefaultTableModel();
        Keranjang.setModel(model);
        addColomnKeranjang();

        model2 = new DefaultTableModel();
        TableHandphone.setModel(model2);
        addColomnHandphone();

        model3 = new DefaultTableModel();
        TableAksesoris.setModel(model3);
        addColomnAksesoris();
        tampilMerk();
        txtUang.setEnabled(false);
        btnBayar.setEnabled(false);
        btnSImpan.setEnabled(false);
        cbMerk.setSelectedItem(null);
        cbMerk.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                tampilIdMerk();
                loadDataAksesoris();
                loadDataHandphone();
            }
        });
        txtCari.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                super.keyPressed(e);
                if(e.getKeyCode() == KeyEvent.VK_ENTER) {
                    loadDataCustomer();
                }
            }
        });
        TableHandphone.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                int i = TableHandphone.getSelectedRow();
                idbarang = (String) model2.getValueAt(i, 0);
                txtNamabarang.setText((String) model2.getValueAt(i, 1));
                cbMerk.setSelectedItem((String) model2.getValueAt(i, 2));
                txtHargabarang.setText((String) model2.getValueAt(i, 3));
                txtStok.setText((String) model2.getValueAt(i, 4));
            }
        });
        TableAksesoris.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                int i = TableAksesoris.getSelectedRow();
                idbarang = (String) model3.getValueAt(i, 0);
                txtNamabarang.setText((String) model3.getValueAt(i, 1));
                cbMerk.setSelectedItem((String) model3.getValueAt(i, 2));
                txtStok.setText((String) model3.getValueAt(i, 4));
                txtHargabarang.setText((String)model3.getValueAt(i, 3));
            }
        });
        txtkuantitas.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                super.keyPressed(e);
                if(e.getKeyCode() == KeyEvent.VK_ENTER) {
                    int qty = Integer.parseInt(txtkuantitas.getText());
                    int stok = Integer.parseInt(txtStok.getText());

                    harga2 = txtHargabarang.getText();
                    String nilai_ribuan = harga2;
                    harga2 = nilai_ribuan.replace(",", "");
                    int harga = Integer.parseInt(harga2);
                    if (qty > stok){
                        JOptionPane.showMessageDialog(null, "Stok Barang tidak mencukupi !", "Peringatan",
                                JOptionPane.WARNING_MESSAGE);
                        txtkuantitas.setText(null);
                    }else {
                        int tempharga = qty * harga;
                        txtHargaakhir.setText(String.valueOf(formatrupiah(tempharga)));
                    }
                }
            }
        });
        btnTambah.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (txtNamabarang.getText().equals("")){
                    JOptionPane.showMessageDialog(null, "Pilih barang yang ingin dibeli !", "Peringatan",
                            JOptionPane.WARNING_MESSAGE);
                }else if (txtkuantitas.getText().equals("")){
                    JOptionPane.showMessageDialog(null, "Isikan kuantitas barang!", "Peringatan",
                            JOptionPane.WARNING_MESSAGE);
                }else if (cbCustomer.getSelectedItem() == null){
                    JOptionPane.showMessageDialog(null, "Pilih data customer !", "Peringatan",
                            JOptionPane.WARNING_MESSAGE);
                }else if (Integer.parseInt(txtkuantitas.getText()) < 1){
                    JOptionPane.showMessageDialog(null, "Minimal pembelian adalah 1 buah!", "Peringatan",
                            JOptionPane.WARNING_MESSAGE);
                }
                else {
                    String harga, harga2;
                    namabarang = txtNamabarang.getText();
                    harga = txtHargabarang.getText();
                    String nilai_ribuan = harga;
                    harga = nilai_ribuan.replace(",", "");

                    harga2 = txtHargaakhir.getText();
                    String nilai_ribuan2 = harga2;
                    harga2 = nilai_ribuan2.replace(",", "");
                    kuantitas = txtkuantitas.getText();

                    Boolean found = false;
                    Object[] obj = new Object[6];
                    obj[0] = idbarang;
                    obj[1] = namabarang;
                    obj[2] = harga;
                    obj[3] = kuantitas;
                    obj[4] = harga2;

                    int j = Keranjang.getModel().getRowCount();
                    for (int k = 0; k < j; k++) {
                        if (obj[1].equals(model.getValueAt(k, 1))) {
                            found = true;
                        }
                    }
                    if (txtHargaakhir.getText().equals("")){
                        JOptionPane.showMessageDialog(null, "Total Harga Kosong !", "Peringatan",
                                JOptionPane.WARNING_MESSAGE);
                    }else if (found) {
                        JOptionPane.showMessageDialog(null, "Data sudah pernah ditambahkan !", "Peringatan",
                                JOptionPane.WARNING_MESSAGE);
                        Clear();
                    } else {
                        model.addRow(obj);
                        totalharga = 0;
                        int row = model.getRowCount();
                        for (int i = 0; i < row; i++) {
                            String temp = (String) model.getValueAt(i, 4);
                            double HT1 = Double.parseDouble(temp);
                            totalharga = HT1 + totalharga;
                        }
                        lblTotalHarga.setText(convertRupiah(totalharga));
                        Clear();
                        DefaultTableModel model2 = (DefaultTableModel) TableAksesoris.getModel();
                        model2.setRowCount(0);
                        DefaultTableModel model3 = (DefaultTableModel) TableHandphone.getModel();
                        model3.setRowCount(0);
                        cbMerk.setSelectedItem(null);
                        txtCari.setText(null);
                        btnBayar.setEnabled(true);
                    }
                }
            }
        });
        btnBayar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                txtUang.setEnabled(true);
                btnTambah.setEnabled(false);
            }
        });
        btnHapus.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Clear();
                DefaultTableModel model2 = (DefaultTableModel) TableAksesoris.getModel();
                model2.setRowCount(0);
                DefaultTableModel model3 = (DefaultTableModel) TableHandphone.getModel();
                model3.setRowCount(0);
                txtCari.setText(null);
                btnSImpan.setEnabled(false);
                txtUang.setEnabled(false);
                btnTambah.setEnabled(true);
            }
        });
        txtUang.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                super.keyReleased(e);
                double kembali = 0;
                try {
                    kembali = RptoDouble(lblTotalHarga.getText());
                } catch (ParseException ex) {
                    ex.printStackTrace();
                }
                double uang = Double.parseDouble(txtUang.getText());
                kembali = uang - kembali;
                txtKembali.setText(convertRupiah(kembali));
                btnSImpan.setEnabled(true);
            }
        });
        btnBatal.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Clear();
                DefaultTableModel model2 = (DefaultTableModel) TableAksesoris.getModel();
                model2.setRowCount(0);
                DefaultTableModel model3 = (DefaultTableModel) TableHandphone.getModel();
                model3.setRowCount(0);
                DefaultTableModel model = (DefaultTableModel) Keranjang.getModel();
                model.setRowCount(0);
                txtCari.setText(null);
                btnBayar.setEnabled(false);
                btnSImpan.setEnabled(false);
                txtUang.setEnabled(false);
                btnTambah.setEnabled(true);
                cbCustomer.removeAllItems();
                cbMerk.setSelectedItem(null);
                txtKembali.setText(null);
                txtUang.setText("");
            }
        });
        btnSImpan.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                tampilIdCustomer();
                autokode();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyMMdd");
                int row = model.getRowCount();
                try {
                    totalharga = RptoDouble(lblTotalHarga.getText());
                } catch (ParseException ex) {
                    ex.printStackTrace();
                }
                idkaryawan = value;

                if (txtUang.getText() == "") {
                    JOptionPane.showMessageDialog(null, "Uang belum diinput !", "Peringatan",
                            JOptionPane.WARNING_MESSAGE);
                } else if (Double.parseDouble(txtUang.getText()) < totalharga) {
                    JOptionPane.showMessageDialog(null, "Jumlah uang kurang !", "Peringatan",
                            JOptionPane.WARNING_MESSAGE);
                    Double.parseDouble(txtUang.getText());
                } else {
                    try {
                        String query = "EXEC sp_InputTransaksiPenjualan @Id_Penjualan=?, @Id_Karyawan=?, @Id_Customer=?, @GrandTotal=?, @Tgl_Penjualan=?";
                        connection.pstat = connection.conn.prepareStatement(query);
                        connection.pstat.setString(1, idtr);
                        connection.pstat.setString(2, idkaryawan);
                        connection.pstat.setString(3, idcustomer);
                        connection.pstat.setDouble(4, totalharga);
                        connection.pstat.setDate(5, java.sql.Date.valueOf((sdf.format(new Date()))));
                        connection.pstat.executeUpdate();

                        for (int i = 0; i < row; i++) {
                            idbarang = (String) model.getValueAt(i, 0);
                            jumlah = (String) model.getValueAt(i, 3);
                            String date = LocalDate.now().plusMonths(12).format(formatter);
                            String sql2 = "EXEC sp_DetailPenjualan @Id_Penjualan=?,@Id_Handphone=?, @Id_Aksesoris=?, @Jumlah=?, @Tgl_Garansi=?";
                            connection.pstat = connection.conn.prepareStatement(sql2);
                            connection.pstat.setString(1, idtr);
                            connection.pstat.setString(2, idbarang);
                            connection.pstat.setString(3, idbarang);
                            connection.pstat.setString(4, jumlah);
                            connection.pstat.setString(5, date);
                            connection.pstat.executeUpdate();
                            connection.pstat.close();
                        }
                        btnPrint.setVisible(true);
                        printnota();
                        JOptionPane.showMessageDialog(null, "Data berhasil disimpan dengan ID " + idtr, "Transaksi Penjualan",
                                JOptionPane.INFORMATION_MESSAGE);
                        autokode();
                        txtUang.setText("");
                        txtKembali.setText("");
                        DefaultTableModel model2 = (DefaultTableModel) TableAksesoris.getModel();
                        model2.setRowCount(0);
                        DefaultTableModel model3 = (DefaultTableModel) TableHandphone.getModel();
                        model3.setRowCount(0);
                        DefaultTableModel model = (DefaultTableModel) Keranjang.getModel();
                        model.setRowCount(0);
                    } catch (Exception ex) {
                        System.out.println("Error saat insert data ke database" + ex);
                    }
                }
            }
        });
        btnPrint.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    tbNota.print();
                } catch (PrinterException ex) {
                }
                tbNota.setVisible(false);
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

    public void printnota(){
        tbNota.removeAll();
        autokode();
        int no = 1;
        int row = model.getRowCount();
        tbNota.setText("=================================================\n");
        tbNota.setText(tbNota.getText() + "==                                         A3 STORE                                        ==\n");
        tbNota.setText(tbNota.getText() + "=================================================\n");
        tbNota.setText(tbNota.getText() + "ID : " + idtr + "\n");
        tbNota.setText(tbNota.getText() + "Date : " + java.sql.Date.valueOf((sdf.format(new Date()))) + "\n\n");
        tbNota.setText(tbNota.getText() + "Items\n");

        for(int i = 0; i<row;i++){
        tbNota.setText(tbNota.getText() + no + ". " + (String) model.getValueAt(i, 1) + " (" + (String) model.getValueAt(i, 3)
                        + ")\n");
        }

        tbNota.setText(tbNota.getText() + "=================================================\n\n");
        tbNota.setText(tbNota.getText() + "\tTotal Harga \t\t: " + lblTotalHarga.getText());
        tbNota.setText(tbNota.getText() + "\n\tBayar \t\t: " +   convertRupiah(Double.parseDouble(txtUang.getText())));
        tbNota.setText(tbNota.getText() + "\n\tKembalian \t\t: " + txtKembali.getText());
        tbNota.setText(tbNota.getText() + "\n=================================================\n");
        tbNota.setText(tbNota.getText() + "==                                    TERIMA KASIH!                                    ==\n");
        tbNota.setText(tbNota.getText() + "=================================================\n");
        tbNota.setVisible(true);
    }

    public void autokode() {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyMMdd");
            String sql = "SELECT * FROM Transaksi_Penjualan ORDER BY Id_Penjualan desc";
            connection.stat = connection.conn.createStatement();
            connection.result = connection.stat.executeQuery(sql);
            String date = (String) LocalDate.now().format(formatter);
            if (connection.result.next()) {
                kode = connection.result.getString("Id_Penjualan").substring(8);
                String AN = "" + (Integer.parseInt(kode) + 1);
                String nol = "";

                if (AN.length() == 1) {
                    nol = "000";
                } else if (AN.length() == 2) {
                    nol = "00";
                } else if (AN.length() == 3) {
                    nol = "0";
                } else if (AN.length() == 4) {
                    nol = "";
                }
                idtr = "TR" + date + nol + AN;

            } else {
                idtr = "TR" + date +"0001";
            }
        } catch (Exception e1) {
            System.out.println("Terjadi error pada autokode: " + e1);
        }
    }

    public void Clear(){
        txtNamabarang.setText(null);
        txtkuantitas.setText(null);
        txtHargaakhir.setText(null);
        txtHargabarang.setText(null);
        txtStok.setText(null);
    }
    public void addColomnKeranjang() {
        model.addColumn("ID");
        model.addColumn("Nama Barang");
        model.addColumn("Harga");
        model.addColumn("Kwantitas");
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

    public void loadDataAksesoris() {
        model3.getDataVector().removeAllElements();
        model3.fireTableDataChanged();
        PreparedStatement st;
        try {
            tampilIdMerk();
            DBConnect connection = new DBConnect();
            connection.stat = connection.conn.createStatement();
            st = connection.conn.prepareStatement("EXEC sp_LoadAksesorisTr @Id_Merk=?");
            st.setString(1,merk);
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                Object[] obj = new Object[5];
                obj[0] = rs.getString(1);
                obj[1] = rs.getString(2);
                obj[2] = rs.getString(3);
                obj[3] =  formatrupiah(rs.getInt(4));
                obj[4] =rs.getString(5);
                model3.addRow(obj);
            }
        } catch (Exception ex) {
            System.out.println("Terjadi error saat load data" + ex);
        }
    }

    public void loadDataCustomer() {
        PreparedStatement st;
        try {
            cbCustomer.removeAllItems();
            DBConnect connection = new DBConnect();
            connection.stat = connection.conn.createStatement();
            st = connection.conn.prepareStatement("EXEC sp_LoadCustomerTr @Nama_Customer=?");
            st.setString(1,txtCari.getText());
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                cbCustomer.addItem(rs.getString("Nama_Customer"));
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
            st = connection.conn.prepareStatement("EXEC sp_LoadHandphoneTr @Id_Merk=?");
            st.setString(1,merk);
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                Object[] obj = new Object[5];
                obj[0] = rs.getString(1);
                obj[1] = rs.getString(2);
                obj[2] = rs.getString(3);
                obj[3] = formatrupiah(rs.getInt(4));
                obj[4] = rs.getString(5);
                model2.addRow(obj);
            }
        } catch (Exception ex) {
            System.out.println("Terjadi error saat load data" + ex);
        }
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

    public void tampilIdCustomer() {
        try {
            DBConnect connection = new DBConnect();
            connection.stat = connection.conn.createStatement();
            String sql1 = "SELECT Id_Customer FROM Customer WHERE Nama_Customer LIKE '%" +
                    cbCustomer.getSelectedItem() + "%'";
            connection.result = connection.stat.executeQuery(sql1);

            while (connection.result.next()) {
                idcustomer = (connection.result.getString("Id_Customer"));
            }
            connection.stat.close();
            connection.result.close();
        } catch (SQLException ex) {
            System.out.println("Terjadi error saat load data" + ex);
        }
    }

}