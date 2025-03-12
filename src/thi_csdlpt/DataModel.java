/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package thi_csdlpt;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.ConnectException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.table.DefaultTableModel;
import org.json.JSONArray;
import org.json.JSONException;

/**
 *
 * @author ROYCE SHEW
 */
public class DataModel {
    public ArrayList<ArrayList<String>> get ( String url ) 
    throws IOException, InterruptedException, JSONException{
     HttpClient client = HttpClient.newHttpClient();
     HttpRequest request;
     request = HttpRequest.newBuilder().uri(URI.create(url)).build();
     HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

     JSONArray ja =new JSONArray(new String(response.body().getBytes(),"utf-8"));
     ArrayList<ArrayList<String>> datalist = new ArrayList<>(); 

     for(int i=0; i<ja.length(); i++){
     String s = ja.get(i).toString();
     s=s.substring(1,s.length()-1);
     String []as = s.split(",");
     ArrayList<String> row = new ArrayList<>();

     for(int j=0;j<as.length;j++){
     as[j]=as[j].replace('"', ' ');
     row.add(as[j]);
     } 
     datalist.add(row);
     }
     return datalist; 
    }
    
    public ArrayList<ArrayList<String>> ket( ArrayList<ArrayList<String>> a,
    ArrayList<ArrayList<String>> b){
     ArrayList<ArrayList<String>> c = new ArrayList<>();
     for(int i=0;i<a.size();i++){
     ArrayList<String> row_a = a.get(i);
     ArrayList<String> row_b = b.get(i);
     if(row_a.get(0).equals(row_b.get(0))){
     ArrayList<String> row_c = new ArrayList<>();
     for(int i1 = 0; i1 < row_a.size() ; i1++){
     row_c.add( row_a.get(i1) );
     }
     for(int i2 = 1; i2 < row_b.size() ; i2++){
     row_c.add( row_b.get(i2));
     }
     c.add(row_c);
     }
     } 
     return c;
    }
    
    public static ArrayList<ArrayList<String>> ket2(ArrayList<ArrayList<String>> a,
                                                   ArrayList<ArrayList<String>> b) {
        ArrayList<ArrayList<String>> c = new ArrayList<>();

        for (ArrayList<String> row_a : a) {
            String maHoaDon_a = row_a.get(0);
            String maSanPham_a = row_a.get(1);

            for (ArrayList<String> row_b : b) {
                String maHoaDon_b = row_b.get(0);
                String maSanPham_b = row_b.get(1);

                // Kiểm tra xem hai hàng có cùng MAHOADON và MASANPHAM hay không
                if (maHoaDon_a.equals(maHoaDon_b) && maSanPham_a.equals(maSanPham_b)) {
                    ArrayList<String> row_c = new ArrayList<>(row_a); // Sao chép row_a
                    for (int i = 2; i < row_b.size(); i++) { // Bỏ qua MAHOADON, MASANPHAM
                        row_c.add(row_b.get(i));
                    }
                    c.add(row_c);
                }
            }
        }
        return c;
    }
    
    public DefaultTableModel getTableModel(String[] tenCot, ArrayList<ArrayList<String>> d){
    DefaultTableModel tableModel = new DefaultTableModel(tenCot, 0);
    for(int i=0;i<d.size();i++){
    Object o[] =new Object[tenCot.length];
    for(int j=0;j<d.get(i).size();j++){
    o[j] = d.get(i).get(j);
    }
    tableModel.addRow(o); 
    }
    return tableModel;
    }
 
    public DefaultTableModel addTableModel( DefaultTableModel tableModel, 
    ArrayList<ArrayList<String>> d,
    String[] tenCot){
     if( tableModel == null){
     tableModel = new DefaultTableModel(tenCot, 0);
     }
     for(int i=0;i<d.size();i++){
     Object o[] =new Object[tenCot.length];
     for(int j=0;j<d.get(i).size();j++){
     o[j] = d.get(i).get(j);
     }
     tableModel.addRow(o); 
     }
     return tableModel;
    }
    
    public ArrayList<ArrayList<String>> getManhSanPham( File f, DefaultTableModel tableModel, JTable tblResult, 
    JTextArea txtError, String tenCot[], String manh ){
    if( f == null ){ return null; }
    ArrayList<String> aIP = new ArrayList();
    try{
    BufferedReader bf = new BufferedReader(new FileReader(f));
    while( bf.ready() ){
    aIP.add( bf.readLine() );
    }
    bf.close();
    }catch(Exception e){
    e.printStackTrace();
    }
    ArrayList<ArrayList<String>> a = null;
    for(int i=0; i < aIP.size(); i++){
    String url="http://" + aIP.get(i)+ "/sanpham/manh"+manh;
    DataModel db = new DataModel(); 
    try{
    a = db.get(url);
    break;
    }
    catch(ConnectException e1){
    String s = txtError.getText();
    s+="\n";
    s+=url+" Down";
    txtError.setText(s);
    }
    catch(Exception e2){
    e2.printStackTrace();
    }
    }
    return a;
    }
    
    public void getDataSanPhamManh( File f, DefaultTableModel tableModel, JTable tblResult, 
    JTextArea txtError, String tenCot[],String manh){
    if( f == null ){ return; }
    ArrayList<String> aIP = new ArrayList();
    try{
    BufferedReader bf = new BufferedReader(new FileReader(f));
    while( bf.ready() ){
    aIP.add( bf.readLine() );
    }
    bf.close();
    }catch(Exception e){
    e.printStackTrace();
    }
    for(int i=0; i < aIP.size(); i++){
    String url="http://" + aIP.get(i)+"/sanpham/manh"+manh;
    DataModel db = new DataModel(); 
    try{
    ArrayList<ArrayList<String>> a = db.get(url);
    DefaultTableModel b = db.addTableModel(tableModel, a,tenCot);
    tblResult.setModel(b);
    break;
    }
    catch(ConnectException e1){
    String s = txtError.getText();
    s+="\n";
    s+=url+" Down";
    txtError.setText(s);
    }
    catch(Exception e2){
    e2.printStackTrace();
    }
    }
    }
    
    public void getDataKhachHangManh( File f, DefaultTableModel tableModel, JTable tblResult, 
    JTextArea txtError, String tenCot[],String manh){
    if( f == null ){ return; }
    ArrayList<String> aIP = new ArrayList();
    try{
    BufferedReader bf = new BufferedReader(new FileReader(f));
    while( bf.ready() ){
    aIP.add( bf.readLine() );
    }
    bf.close();
    }catch(Exception e){
    e.printStackTrace();
    }
    for(int i=0; i < aIP.size(); i++){
    String url="http://" + aIP.get(i)+"/khachhang/manh"+manh;
    DataModel db = new DataModel(); 
    try{
    ArrayList<ArrayList<String>> a = db.get(url);
    DefaultTableModel b = db.addTableModel(tableModel, a,tenCot);
    tblResult.setModel(b);
    break;
    }
    catch(ConnectException e1){
    String s = txtError.getText();
    s+="\n";
    s+=url+" Down";
    txtError.setText(s);
    }
    catch(Exception e2){
    e2.printStackTrace();
    }
    }
    }
    
    public ArrayList<ArrayList<String>> getManhKhachHang( File f, DefaultTableModel tableModel, JTable tblResult, 
    JTextArea txtError, String tenCot[], String manh ){
    if( f == null ){ return null; }
    ArrayList<String> aIP = new ArrayList();
    try{
    BufferedReader bf = new BufferedReader(new FileReader(f));
    while( bf.ready() ){
    aIP.add( bf.readLine() );
    }
    bf.close();
    }catch(Exception e){
    e.printStackTrace();
    }
    ArrayList<ArrayList<String>> a = null;
    for(int i=0; i < aIP.size(); i++){
    String url="http://" + aIP.get(i)+ "/khachhang/manh"+manh;
    DataModel db = new DataModel(); 
    try{
    a = db.get(url);
    break;
    }
    catch(ConnectException e1){
    String s = txtError.getText();
    s+="\n";
    s+=url+" Down";
    txtError.setText(s);
    }
    catch(Exception e2){
    e2.printStackTrace();
    }
    }
    return a;
    }
    
    public void getDataKhoHangManh( File f, DefaultTableModel tableModel, JTable tblResult, 
    JTextArea txtError, String tenCot[],String manh){
    if( f == null ){ return; }
    ArrayList<String> aIP = new ArrayList();
    try{
    BufferedReader bf = new BufferedReader(new FileReader(f));
    while( bf.ready() ){
    aIP.add( bf.readLine() );
    }
    bf.close();
    }catch(Exception e){
    e.printStackTrace();
    }
    for(int i=0; i < aIP.size(); i++){
    String url="http://" + aIP.get(i)+"/khohang/manh"+manh;
    DataModel db = new DataModel(); 
    try{
    ArrayList<ArrayList<String>> a = db.get(url);
    DefaultTableModel b = db.addTableModel(tableModel, a,tenCot);
    tblResult.setModel(b);
    break;
    }
    catch(ConnectException e1){
    String s = txtError.getText();
    s+="\n";
    s+=url+" Down";
    txtError.setText(s);
    }
    catch(Exception e2){
    e2.printStackTrace();
    }
    }
    }
    
    public ArrayList<ArrayList<String>> getManhKhoHang( File f, DefaultTableModel tableModel, JTable tblResult, 
    JTextArea txtError, String tenCot[], String manh ){
    if( f == null ){ return null; }
    ArrayList<String> aIP = new ArrayList();
    try{
    BufferedReader bf = new BufferedReader(new FileReader(f));
    while( bf.ready() ){
    aIP.add( bf.readLine() );
    }
    bf.close();
    }catch(Exception e){
    e.printStackTrace();
    }
    ArrayList<ArrayList<String>> a = null;
    for(int i=0; i < aIP.size(); i++){
    String url="http://" + aIP.get(i)+ "/khohang/manh"+manh;
    DataModel db = new DataModel(); 
    try{
    a = db.get(url);
    break;
    }
    catch(ConnectException e1){
    String s = txtError.getText();
    s+="\n";
    s+=url+" Down";
    txtError.setText(s);
    }
    catch(Exception e2){
    e2.printStackTrace();
    }
    }
    return a;
    }
    
    public ArrayList<ArrayList<String>> getManhHoaDon( File f, DefaultTableModel tableModel, JTable tblResult, 
    JTextArea txtError, String tenCot[], String manh ){
    if( f == null ){ return null; }
    ArrayList<String> aIP = new ArrayList();
    try{
    BufferedReader bf = new BufferedReader(new FileReader(f));
    while( bf.ready() ){
    aIP.add( bf.readLine() );
    }
    bf.close();
    }catch(Exception e){
    e.printStackTrace();
    }
    ArrayList<ArrayList<String>> a = null;
    for(int i=0; i < aIP.size(); i++){
    String url="http://" + aIP.get(i)+ "/hoadon/manh"+manh;
    DataModel db = new DataModel(); 
    try{
    a = db.get(url);
    break;
    }
    catch(ConnectException e1){
    String s = txtError.getText();
    s+="\n";
    s+=url+" Down";
    txtError.setText(s);
    }
    catch(Exception e2){
    e2.printStackTrace();
    }
    }
    return a;
    }
    
    public void getDataHoaDonManh( File f, DefaultTableModel tableModel, JTable tblResult, 
    JTextArea txtError, String tenCot[],String manh){
    if( f == null ){ return; }
    ArrayList<String> aIP = new ArrayList();
    try{
    BufferedReader bf = new BufferedReader(new FileReader(f));
    while( bf.ready() ){
    aIP.add( bf.readLine() );
    }
    bf.close();
    }catch(Exception e){
    e.printStackTrace();
    }
    for(int i=0; i < aIP.size(); i++){
    String url="http://" + aIP.get(i)+"/hoadon/manh"+manh;
    DataModel db = new DataModel(); 
    try{
    ArrayList<ArrayList<String>> a = db.get(url);
    DefaultTableModel b = db.addTableModel(tableModel, a,tenCot);
    tblResult.setModel(b);
    break;
    }
    catch(ConnectException e1){
    String s = txtError.getText();
    s+="\n";
    s+=url+" Down";
    txtError.setText(s);
    }
    catch(Exception e2){
    e2.printStackTrace();
    }
    }
    }
    
    public void getDataChiTietHoaDonManh( File f, DefaultTableModel tableModel, JTable tblResult, 
    JTextArea txtError, String tenCot[],String manh){
    if( f == null ){ return; }
    ArrayList<String> aIP = new ArrayList();
    try{
    BufferedReader bf = new BufferedReader(new FileReader(f));
    while( bf.ready() ){
    aIP.add( bf.readLine() );
    }
    bf.close();
    }catch(Exception e){
    e.printStackTrace();
    }
    for(int i=0; i < aIP.size(); i++){
    String url="http://" + aIP.get(i)+"/chitiethoadon/manh"+manh;
    DataModel db = new DataModel(); 
    try{
    ArrayList<ArrayList<String>> a = db.get(url);
    DefaultTableModel b = db.addTableModel(tableModel, a,tenCot);
    tblResult.setModel(b);
    break;
    }
    catch(ConnectException e1){
    String s = txtError.getText();
    s+="\n";
    s+=url+" Down";
    txtError.setText(s);
    }
    catch(Exception e2){
    e2.printStackTrace();
    }
    }
    }
    
        public ArrayList<ArrayList<String>> getManhChiTietHoaDon( File f, DefaultTableModel tableModel, JTable tblResult, 
    JTextArea txtError, String tenCot[], String manh ){
    if( f == null ){ return null; }
    ArrayList<String> aIP = new ArrayList();
    try{
    BufferedReader bf = new BufferedReader(new FileReader(f));
    while( bf.ready() ){
    aIP.add( bf.readLine() );
    }
    bf.close();
    }catch(Exception e){
    e.printStackTrace();
    }
    ArrayList<ArrayList<String>> a = null;
    for(int i=0; i < aIP.size(); i++){
    String url="http://" + aIP.get(i)+ "/chitiethoadon/manh"+manh;
    DataModel db = new DataModel(); 
    try{
    a = db.get(url);
    break;
    }
    catch(ConnectException e1){
    String s = txtError.getText();
    s+="\n";
    s+=url+" Down";
    txtError.setText(s);
    }
    catch(Exception e2){
    e2.printStackTrace();
    }
    }
    return a;
    }
    
}
