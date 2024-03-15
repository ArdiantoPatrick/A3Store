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
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;

public class TransaksiService {
    private JTable tabelGaransi;
    private JTextField txtCari;
    private JButton btnTambah;
    private JButton btnClear;
    private JTable tabelKeranjang;
    private JTextField txtUang;
    private JButton btnBatal;
    private JButton btnSimpan;
    private JTable tabelService;
    private JRadioButton rbGaransi;
    private JComboBox cbTeknisi;
    private JComboBox cbMerk;
    private JComboBox cbJenisService;
    private JRadioButton rbTidak;
    private JLabel lblTotalHarga;
    private JTextField txtHarga;
    private JTextField txtNamaService;
    private JButton btnBayar;
    public JPanel TransaksiService;
    private JTextField txtKembali;
    private JTextArea tbNota;
    private JButton btnPrint;

    String kode,merk,teknisi,jenisservice,service,idgaransi,id,namaservice;
    double totalharga,subtotal;
    Date garansi, datenow;
    DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    DBConnect connection = new DBConnect();
    private DefaultTableModel model = new DefaultTableModel();
    private DefaultTableModel model2 = new DefaultTableModel();
    private DefaultTableModel model3 = new DefaultTableModel();

    public TransaksiService() {
        namaservice = txtNamaService.getText();
        tbNota.setVisible(false);
        btnPrint.setVisible(false);
        autokode();
        radiobutton();
        FrameRadioButton();
        txtCari.setEnabled(false);
        cbJenisService.setEnabled(false);
        model = new DefaultTableModel();
        tabelGaransi.setModel(model);
        addColomnGaransi();

        model2 = new DefaultTableModel();
        tabelKeranjang.setModel(model2);
        addColomnKeranjang();

        model3 = new DefaultTableModel();
        tabelService.setModel(model3);
        addColomnSevice();

        tampilMerk();
        tampilTeknisi();
        tampilJenisService();
        cbTeknisi.setSelectedItem(null);
        cbMerk.setSelectedItem(null);
        cbJenisService.setSelectedItem(null);
        txtCari.setEnabled(false);
        cbJenisService.setEnabled(false);
        cbTeknisi.setEnabled(false);
        cbMerk.setEnabled(false);
        txtUang.setEnabled(false);
        btnSimpan.setEnabled(false);
        btnBayar.setEnabled(false);
        rbGaransi.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                txtCari.setEnabled(true);
                cbTeknisi.setEnabled(true);
                cbMerk.setEnabled(true);
            }
        });
        txtCari.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                super.keyPressed(e);
                if(e.getKeyCode() == KeyEvent.VK_ENTER) {
                    loadDataGaransi();
                }
            }
        });
        cbMerk.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cbJenisService.setEnabled(true);
                cbJenisService.setSelectedItem(null);
            }
        });
        rbTidak.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                txtCari.setEnabled(false);
                cbTeknisi.setEnabled(true);
                cbMerk.setEnabled(true);
                DefaultTableModel model = (DefaultTableModel) tabelGaransi.getModel();
                model.setRowCount(0);
            }
        });
        cbJenisService.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                DefaultTableModel model3 = (DefaultTableModel) tabelService.getModel();
                model3.setRowCount(0);
                tampilService();
            }
        });
        tabelGaransi.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                datenow = new Date();
                String garansi2;
                int i = tabelGaransi.getSelectedRow();
                try {
                    garansi2 = (String) model.getValueAt(i,4);
                    garansi = sdf.parse(garansi2);
                    if (datenow.compareTo(garansi) > 0){
                        JOptionPane.showMessageDialog(null, "Garansi sudah tidak berlaku sejak " + garansi2 + " !", "Peringatan",
                                JOptionPane.WARNING_MESSAGE);
                        model.setRowCount(0);
                        txtCari.setText(null);
                    }else {
                        cbMerk.setSelectedItem((String) model.getValueAt(i, 2));
                        cbMerk.setEnabled(false);
                    }
                }catch (Exception ex){
                    System.out.println(ex);
                }
            }
        });
        tabelService.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                int i = tabelService.getSelectedRow();
                String service2 = cbJenisService.getSelectedItem().toString();

                if (rbGaransi.isSelected() && service2.equals("Sistem")){
                    txtHarga.setText("0");
                    service = (String) model3.getValueAt(i, 0);
                    txtNamaService.setText((String) model3.getValueAt(i, 3));
                }else{
                    service = (String) model3.getValueAt(i, 0);
                    txtNamaService.setText((String) model3.getValueAt(i, 3));
                    txtHarga.setText((String) model3.getValueAt(i, 4));
                }
            }
        });
        btnClear.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                txtNamaService.setText(null);
                txtHarga.setText(null);
                btnTambah.setEnabled(true);
                txtUang.setEnabled(false);
                cbMerk.setSelectedItem(null);
                cbJenisService.setSelectedItem(null);
                cbTeknisi.setSelectedItem(null);
            }
        });
        btnBayar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                btnTambah.setEnabled(false);
                txtUang.setEnabled(true);
            }
        });
        btnBatal.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clear();
                DefaultTableModel model = (DefaultTableModel) tabelService.getModel();
                model.setRowCount(0);
                DefaultTableModel model2 = (DefaultTableModel) tabelGaransi.getModel();
                model2.setRowCount(0);
                DefaultTableModel model3 = (DefaultTableModel) tabelKeranjang.getModel();
                model3.setRowCount(0);
            }
        });
        btnTambah.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                tampilIdMerk();
                tampilIdJenisService();
                tampilIdService();
                if (rbGaransi.isSelected()) {
                    idgaransi = txtCari.getText();
                } else if (rbTidak.isSelected()) {
                    idgaransi = "NULL";
                }
                if (idgaransi == null || merk == null || cbJenisService.getSelectedItem() == null || txtHarga.getText() == null ||
                        cbTeknisi.getSelectedItem() == null || namaservice == null) {
                    JOptionPane.showMessageDialog(null, "Lengkapi Semua Data !", "Peringatan",
                            JOptionPane.WARNING_MESSAGE);
                } else {
                    String harga;
                    harga = txtHarga.getText();
                    String nilai_ribuan = harga;
                    harga = nilai_ribuan.replace(",", "");

                    jenisservice = cbJenisService.getSelectedItem().toString();
                    teknisi = cbTeknisi.getSelectedItem().toString();
                    Boolean found = false;
                    Object[] obj = new Object[6];
                    obj[0] = idgaransi;
                    obj[1] = merk;
                    obj[2] = jenisservice;
                    obj[3] = service;
                    obj[4] = txtNamaService.getText();
                    obj[5] = harga;

                    int j = tabelKeranjang.getModel().getRowCount();
                    for (int k = 0; k < j; k++) {
                        if (obj[4].equals(model2.getValueAt(k, 4))) {
                            found = true;
                        }
                    }
                    if (found) {
                        JOptionPane.showMessageDialog(null, "Service Sudah Ada!", "Peringatan",
                                JOptionPane.WARNING_MESSAGE);
                        txtNamaService.setText(null);
                        txtHarga.setText(null);
                    } else {
                        model2.addRow(obj);
                        totalharga = 0;
                        int row = model2.getRowCount();
                        for (int i = 0; i < row; i++) {
                            String temp = (String) model2.getValueAt(i, 5);
                            double HT1 = Double.parseDouble(temp);
                            totalharga = HT1 + totalharga;
                        }
                        lblTotalHarga.setText(convertRupiah(totalharga));
                        cbTeknisi.setEnabled(false);
                        cbMerk.setEnabled(false);
                        rbGaransi.setEnabled(false);
                        rbTidak.setEnabled(false);
                        cbJenisService.setSelectedItem(null);
                        txtNamaService.setText(null);
                        txtHarga.setText(null);
                        DefaultTableModel model3 = (DefaultTableModel) tabelService.getModel();
                        model3.setRowCount(0);
                        btnBayar.setEnabled(true);
                    }
                }
            }
        });

        btnSimpan.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                tampilIdService();
                tampilIdJenisService();
                tampilIdMerk();
                tampilIdTeknisi();
                int row = model2.getRowCount();
                int k = 0;
                try {
                    totalharga = RptoDouble(lblTotalHarga.getText());
                } catch (ParseException ex) {
                    ex.printStackTrace();
                }
                idgaransi = (String) model2.getValueAt(k, 0);
                double kembali = 0;
                double uang = Double.parseDouble(txtUang.getText());

                if (txtUang.getText() == "") {
                    JOptionPane.showMessageDialog(null, "Uang belum diinput !", "Peringatan",
                            JOptionPane.WARNING_MESSAGE);
                } else if (uang < totalharga) {
                    JOptionPane.showMessageDialog(null, "Jumlah uang kurang !", "Peringatan",
                            JOptionPane.WARNING_MESSAGE);
                } else {
                    try {
                        String query = "EXEC sp_InputTransaksiService @Id_TrService=?, @Id_Karyawan=?, @Id_Merk=?, @GrandTotal=?, @Tgl_Service=?";
                        connection.pstat = connection.conn.prepareStatement(query);
                        connection.pstat.setString(1, id);
                        connection.pstat.setString(2, teknisi);
                        connection.pstat.setString(3, merk);
                        connection.pstat.setDouble(4, totalharga);
                        connection.pstat.setDate(5, java.sql.Date.valueOf((sdf.format(new Date()))));
                        connection.pstat.executeUpdate();

                        for (int i = 0; i < row; i++) {
                            service = (String) model2.getValueAt(i, 3);
                            namaservice = (String) model2.getValueAt(i, 4);
                            subtotal = Double.parseDouble((String) model2.getValueAt(i, 5));
                            String sql2 = "EXEC sp_DetailService @Id_TrService=?,@Id_Penjualan=?, @Id_Service=?, @Nama_Service=?, @SubTotal=?";
                            connection.pstat = connection.conn.prepareStatement(sql2);
                            connection.pstat.setString(1, id);
                            connection.pstat.setString(2, idgaransi);
                            connection.pstat.setString(3, service);
                            connection.pstat.setString(4, namaservice);
                            connection.pstat.setDouble(5, subtotal);
                            connection.pstat.executeUpdate();
                            connection.pstat.close();
                        }
                        printnota();
                        btnPrint.setVisible(true);
                        JOptionPane.showMessageDialog(null, "Data berhasil disimpan dengan ID " + id, "Transaksi Penjualan",
                                JOptionPane.INFORMATION_MESSAGE);
                        autokode();
                        clear();
                    } catch (Exception ex) {
                        System.out.println("Error saat insert data ke database" + ex);
                    }
                }
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
                btnSimpan.setEnabled(true);
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

    public void printnota(){
        tbNota.removeAll();
        autokode();
        int no = 1;
        int row = model2.getRowCount();
        tbNota.setText("=================================================\n");
        tbNota.setText(tbNota.getText() + "==                                         A3 STORE                                        ==\n");
        tbNota.setText(tbNota.getText() + "=================================================\n");
        tbNota.setText(tbNota.getText() + "ID : " + id + "\n");
        tbNota.setText(tbNota.getText() + "Date : " + java.sql.Date.valueOf((sdf.format(new Date()))) + "\n\n");
        tbNota.setText(tbNota.getText() + "Items\n");

        for(int i = 0; i<row;i++){
            tbNota.setText(tbNota.getText() + no + ". " + (String) model2.getValueAt(i, 4) + "\n");
            no ++;
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

    public void addColomnGaransi () {
        model.addColumn("Id Garansi");
        model.addColumn("Id Handphone");
        model.addColumn("Merk");
        model.addColumn("Jumlah Handphone");
        model.addColumn("Tanggal Garansi");
    }

    public void addColomnKeranjang () {
        model2.addColumn("Garansi");
        model2.addColumn("Merk");
        model2.addColumn("Jenis Service");
        model2.addColumn("Id Service");
        model2.addColumn("Service");
        model2.addColumn("Harga");
    }

    public void addColomnSevice () {
        model3.addColumn("Id Service");
        model3.addColumn("Jenis Service");
        model3.addColumn("Merk");
        model3.addColumn("Nama Service");
        model3.addColumn("Harga");
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
        txtUang.setText(null);
        btnBayar.setEnabled(false);
        txtKembali.setText(null);
        btnSimpan.setEnabled(false);
        btnTambah.setEnabled(true);
        rbGaransi.setEnabled(true);
        rbTidak.setEnabled(true);
        rbGaransi.setSelected(false);
        rbTidak.setSelected(false);
        cbMerk.setEnabled(false);
        cbTeknisi.setEnabled(false);
        lblTotalHarga.setText("0");
        txtCari.setText(null);
        txtCari.setEnabled(false);
        cbMerk.setSelectedItem(null);
        cbJenisService.setSelectedItem(null);
        cbJenisService.setEnabled(false);
        txtHarga.setText(null);
        txtNamaService.setText(null);
        cbTeknisi.setSelectedItem(null);

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

    public void autokode() {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyMMdd");
            String sql = "SELECT * FROM Transaksi_Service ORDER BY Id_TrService desc";
            connection.stat = connection.conn.createStatement();
            connection.result = connection.stat.executeQuery(sql);
            if (connection.result.next()) {
                kode = connection.result.getString("Id_TrService").substring(4);
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
                id = "TJ" + nol + AN;

            } else {
                id = "TJ"+ "0001";
            }
        } catch (Exception e1) {
            System.out.println("Terjadi error pada autokode2: " + e1);
        }
    }

    public void tampilService() {
        PreparedStatement st;
        try {
            tampilIdMerk();
            tampilIdJenisService();
            DBConnect connection = new DBConnect();
            connection.stat = connection.conn.createStatement();
            st = connection.conn.prepareStatement("EXEC sp_LoadServiceTr @Id_Merk=?, @Id_Jenis_Service=?");
            st.setString(1,merk);
            st.setString(2,jenisservice);
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                Object[] obj = new Object[5];
                obj[0] = rs.getString(1);
                obj[1] = rs.getString(2);
                obj[2] = rs.getString(3);
                obj[3] = rs.getString(4);
                obj[4] = formatrupiah(rs.getInt(5));
                model3.addRow(obj);
            }
            connection.stat.close();
        } catch (SQLException ex) {
            System.out.println("Terjadi error saat load data Service " + ex);
        }
    }

    public void tampilIdService() {
        try {
            DBConnect connection = new DBConnect();
            connection.stat = connection.conn.createStatement();
            String sql1 = "SELECT Id_Service FROM Service WHERE Nama_Service LIKE '%" +
                    txtNamaService.getText() + "%'";
            connection.result = connection.stat.executeQuery(sql1);

            while (connection.result.next()) {
                service = (connection.result.getString("Id_Service"));
            }
            connection.stat.close();
            connection.result.close();
        } catch (SQLException ex) {
            System.out.println("Terjadi error saat load data" + ex);
        }
    }

    public void tampilTeknisi() {
        try {
            DBConnect connection = new DBConnect();
            connection.stat = connection.conn.createStatement();
            String sql1 = "SELECT Nama_Karyawan FROM Karyawan WHERE Status = 1 AND Id_Jabatan = 'JK0004'";
            connection.result = connection.stat.executeQuery(sql1);

            while (connection.result.next()) {
                cbTeknisi.addItem(connection.result.getString("Nama_Karyawan"));
            }
            connection.stat.close();
            connection.result.close();
        } catch (SQLException ex) {
            System.out.println("Terjadi error saat load data ID Merk" + ex);
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

    public void tampilIdTeknisi() {
        try {
            DBConnect connection = new DBConnect();
            connection.stat = connection.conn.createStatement();
            String sql1 = "SELECT Id_Karyawan FROM Karyawan WHERE Nama_Karyawan LIKE '%" +
                    cbTeknisi.getSelectedItem() + "%'";
            connection.result = connection.stat.executeQuery(sql1);

            while (connection.result.next()) {
                teknisi = (connection.result.getString("Id_Karyawan"));
            }
            connection.stat.close();
            connection.result.close();
        } catch (SQLException ex) {
            System.out.println("Terjadi error saat load data" + ex);
        }
    }

    public void tampilJenisService() {
        try {
            DBConnect connection = new DBConnect();
            connection.stat = connection.conn.createStatement();
            String sql1 = "SELECT Nama_Jenis_Service FROM Jenis_Service WHERE Status = 1";
            connection.result = connection.stat.executeQuery(sql1);

            while (connection.result.next()) {
                cbJenisService.addItem(connection.result.getString("Nama_Jenis_Service"));
            }
            connection.stat.close();
            connection.result.close();
        } catch (SQLException ex) {
            System.out.println("Terjadi error saat load data ID Merk" + ex);
        }
    }

    public void tampilIdJenisService() {
        try {
            DBConnect connection = new DBConnect();
            connection.stat = connection.conn.createStatement();
            String sql1 = "SELECT Id_Jenis_Service FROM Jenis_Service WHERE Nama_Jenis_Service LIKE '%" +
                    cbJenisService.getSelectedItem() + "%'";
            connection.result = connection.stat.executeQuery(sql1);

            while (connection.result.next()) {
                jenisservice = (connection.result.getString("Id_Jenis_Service"));
            }
            connection.stat.close();
            connection.result.close();
        } catch (SQLException ex) {
            System.out.println("Terjadi error saat load data" + ex);
        }
    }

    public void radiobutton(){
        if (rbGaransi.isSelected()){
            rbTidak.setSelected(false);
            rbTidak.enable(false);
        }else{
            rbGaransi.setSelected(false);
            rbGaransi.enable(false);
        }
    }

    public void FrameRadioButton(){
        ButtonGroup grup = new ButtonGroup();
        grup.add(rbGaransi);
        grup.add(rbTidak);
    }

    public void loadDataGaransi(){
        //menghapus seluruh data ditampilkan(jika ada) untuk tampilan pertama
        int i = tabelGaransi.getSelectedRow();
        model.getDataVector().removeAllElements();
        //memberi tahu data yang kosong
        model.fireTableDataChanged();
        PreparedStatement st;
        try{
            connection.stat = connection.conn.createStatement();
            st = connection.conn.prepareStatement("EXEC sp_LoadGaransi @Id_Penjualan=?");
            st.setString(1,txtCari.getText());
            ResultSet rs = st.executeQuery();

            //lakukan baris perbaris
            while(rs.next()){
                Object[] obj = new Object[5];
                obj[0] = rs.getString(1);
                obj[1] = rs.getString(2);
                obj[2] = rs.getString(3);
                obj[3] = rs.getInt(4);
                obj[4] = rs.getString(5);
                model.addRow(obj);
            }
            connection.stat.close();
            connection.result.close();
        }
        catch (Exception e){
            System.out.println("Terjadi error saat load data garansi: "+e);
        }
    }

}
