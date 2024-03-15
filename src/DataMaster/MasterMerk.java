package DataMaster;

import DBConnect.DBConnect;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MasterMerk extends JFrame{
    public JPanel JPMerk;
    public JPanel JPMerk1;
    private JTextField txtID;
    private JTextField txtNama;
    private JTextField txtCari;
    private JTable tableView;
    private JButton btnSimpan;
    private JButton btnUbah;
    private JButton btnCari;
    private JButton btnHapus;
    private JButton btnBatal;
    private JPanel JPJabatanKaryawan;

    //table
    private DefaultTableModel model;

    //connection database
    DBConnect connection = new DBConnect();

    //variabel global
    int status;
    String id,nama,autokode;

    public MasterMerk(){
        this.setSize(1920, 1080);
        model = new DefaultTableModel();
        tableView.setModel(model);
        setContentPane(JPMerk);
        addColomn();
        autokode();
        loadData();
        btnSimpan.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                id = txtID.getText();
                nama = txtNama.getText();
                status = 1;
                Boolean found = false;

                //Validasi Jika Memasukkan Nama Yang sama
                Object[] obj = new Object[2];
                obj[0] = id;
                obj[1] = txtNama.getText();
                int j = tableView.getModel().getRowCount();
                for (int k = 0; k < j; k++) {
                    if(obj[1].equals(model.getValueAt(k,1)))
                    {
                        found = true;
                    }
                }
                if(found) {
                    JOptionPane.showMessageDialog(null, "Deskripsi Merk Sudah Ada!", "Peringatan",
                            JOptionPane.WARNING_MESSAGE);
                    txtNama.setText("");
                }else{
                    try{
                        if (id.equals("") | nama.equals("")){
                            JOptionPane.showMessageDialog(null, "Mohon untuk mengisi seluruh data!");
                        }else {

                            String query2 = "EXEC sp_InsertMerk @Id_Merk=?, @Nama_Merk=?";
                            connection.pstat = connection.conn.prepareStatement(query2);
                            connection.pstat.setString(1, id);
                            connection.pstat.setString(2, nama);
                            connection.pstat.executeUpdate();
                            connection.pstat.close();
                            JOptionPane.showMessageDialog(null, "Data Berhasil Diinput");
                            reset();
                        }
                    }catch (Exception ex){
                        System.out.println("Terjadi Erorr pada saat insert data  : " + ex);
                    }
                }
            }
        });
        btnUbah.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                id = txtID.getText();
                nama = txtNama.getText();
                try {
                    if(id.equals("") | nama.equals("")) {
                        throw new Exception("Mohon untuk mengisi seluruh data!");
                    } else {
                        try {
                            String query = "UPDATE Merk SET Nama_Merk=? WHERE Id_Merk=?";
                            connection.pstat = connection.conn.prepareStatement(query);
                            connection.pstat.setString(2, id);
                            connection.pstat.setString(1, nama);
                            connection.pstat.executeUpdate();
                            connection.pstat.close();
                            JOptionPane.showMessageDialog(null, "Data Berhasil Diubah");
                            clear();
                        } catch (Exception ex) {
                            System.out.println("Error saat update data ke database" + ex);
                        }
                    }
                }catch (Exception ex){
                    JOptionPane.showMessageDialog(null,ex.getMessage());
                }
                reset();
            }
        });
        tableView.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                int i = tableView.getSelectedRow();
                txtID.setText((String) model.getValueAt(i,0));
                txtNama.setText((String) model.getValueAt(i,1));
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
                    String query = "SELECT * FROM Merk WHERE Id_Merk = '" + txtCari.getText() + "' AND Status = 1";
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
                txtCari.setText("");
                reset();
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
                            String query = "EXEC sp_DeleteMerk @Id_Merk =?";
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
    }

    //header table
    public void addColomn(){
        model.addColumn("ID Merk");
        model.addColumn("Nama Merk");
    }

    //method reset
    public void reset(){
        loadData();
        autokode();
        txtNama.setText(null);
    }

    public void clear(){
        txtID.setText("");
        txtNama.setText("");
    }

    public void autokode(){
        try{
            String sql = "SELECT * FROM Merk ORDER BY Id_Merk desc";
            connection.stat = connection.conn.createStatement();
            connection.result = connection.stat.executeQuery(sql);
            if (connection.result.next()) {
                autokode = connection.result.getString("Id_Merk").substring(4);
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
                txtID.setText("MR" + nol + AN);
                txtID.setEnabled(false);

            }else {
                txtID.setText("MR001");
                txtID.setEnabled(false);
            }
            connection.stat.close();
            connection.result.close();
        }catch (Exception e1){
            System.out.println("Terjadi error pada CRUD: " + e1);
        }
    }

    public void loadData(){
        //menghapus seluruh data ditampilkan(jika ada) untuk tampilan pertama
        model.getDataVector().removeAllElements();
        //memberi tahu data yang kosong
        model.fireTableDataChanged();
        try{
            connection.stat = connection.conn.createStatement();
            String query = "SELECT * FROM Merk WHERE Status = 1";
            connection.result = connection.stat.executeQuery(query);
            //lakukan baris perbaris
            while(connection.result.next()){
                String TempStatus = "";
                Object[] obj = new  Object[2];
                obj[0] = connection.result.getString("Id_Merk");
                obj[1] = connection.result.getString("Nama_Merk");
                model.addRow(obj);
            }
            connection.stat.close();
            connection.result.close();
        }
        catch (Exception e){
            System.out.println("Terjadi error saat load data member: "+e);
        }
    }
}
