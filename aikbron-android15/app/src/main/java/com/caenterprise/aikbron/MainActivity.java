package com.caenterprise.aikbron;
import android.app.Activity;
import android.app.DownloadManager;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.webkit.CookieManager;
import android.webkit.PermissionRequest;
import android.webkit.SslErrorHandler;
import android.net.http.SslError;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.view.WindowInsets;
import android.widget.Toast;
import java.util.Locale;
public class MainActivity extends Activity {
  private WebView browser;
  private String permittedOrigin=null;
  private ValueCallback<Uri[]> upload;
  private static final int PICK_FILES=1304;
  @Override public void onCreate(Bundle saved){
    super.onCreate(saved);
    browser=new WebView(this);
    WebSettings st=browser.getSettings();
    st.setJavaScriptEnabled(true);
    st.setDomStorageEnabled(true);
    st.setAllowFileAccess(false);
    st.setAllowContentAccess(true);
    st.setAllowFileAccessFromFileURLs(false);
    st.setAllowUniversalAccessFromFileURLs(false);
    st.setMixedContentMode(WebSettings.MIXED_CONTENT_NEVER_ALLOW);
    CookieManager.getInstance().setAcceptCookie(true);
    browser.setWebViewClient(new WebViewClient(){
      @Override public boolean shouldOverrideUrlLoading(WebView w, android.webkit.WebResourceRequest req){
        Uri uri=req.getUrl();
        if(uri.toString().equals("file:///android_asset/start.html"))return false;
        if(allow(uri))return false;
        if("https".equalsIgnoreCase(uri.getScheme())){
          try{ startActivity(new Intent(Intent.ACTION_VIEW,uri)); }catch(ActivityNotFoundException e){}
        }
        return true;
      }
      @Override public void onReceivedSslError(WebView v,SslErrorHandler h,SslError e){h.cancel();}
    });
    browser.setWebChromeClient(new WebChromeClient(){
      @Override public boolean onShowFileChooser(WebView v,ValueCallback<Uri[]> cb,FileChooserParams params){
        if(upload!=null)upload.onReceiveValue(null);
        upload=cb;
        try {
          Intent i=params.createIntent();
          i.addCategory(Intent.CATEGORY_OPENABLE);
          startActivityForResult(i,PICK_FILES);
        }catch(Exception e){upload=null;cb.onReceiveValue(null);Toast.makeText(MainActivity.this,"Selector no disponible",Toast.LENGTH_SHORT).show();}
        return true;
      }
      @Override public void onPermissionRequest(PermissionRequest r){r.deny();}
    });
    browser.setDownloadListener((url,ua,cd,mime,length)->{
      Uri uri=Uri.parse(url);
      if(permittedOrigin==null || !permittedOrigin.equals(origin(uri)))return;
      try{
        DownloadManager.Request request=new DownloadManager.Request(uri);
        request.setMimeType(mime);
        String cookie=CookieManager.getInstance().getCookie(url);
        if(cookie!=null)request.addRequestHeader("Cookie",cookie);
        request.addRequestHeader("User-Agent",ua);
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setDestinationInExternalFilesDir(MainActivity.this, Environment.DIRECTORY_DOWNLOADS,"aiKBron-"+System.currentTimeMillis());
        ((DownloadManager)getSystemService(Context.DOWNLOAD_SERVICE)).enqueue(request);
        Toast.makeText(MainActivity.this,"Descarga iniciada",Toast.LENGTH_SHORT).show();
      }catch(Exception e){Toast.makeText(MainActivity.this,"No se pudo descargar",Toast.LENGTH_SHORT).show();}
    });
    setContentView(browser);
    if(Build.VERSION.SDK_INT>=30)browser.setOnApplyWindowInsetsListener((view,insets)->{
      android.graphics.Insets b=insets.getInsets(WindowInsets.Type.systemBars());
      view.setPadding(b.left,b.top,b.right,b.bottom);return insets;
    });
    browser.loadUrl("file:///android_asset/start.html");
  }
  private String origin(Uri u){return u.getScheme()+"://"+u.getAuthority();}
  private boolean allow(Uri u){
    String scheme=u.getScheme(), host=u.getHost();
    if(scheme==null||host==null||u.getUserInfo()!=null)return false;
    scheme=scheme.toLowerCase(Locale.ROOT);host=host.toLowerCase(Locale.ROOT);
    if(!"https".equals(scheme)&&!"http".equals(scheme))return false;
    if("http".equals(scheme)&&!privateIp(host))return false;
    String candidate=origin(u);
    if(permittedOrigin==null)permittedOrigin=candidate;
    return permittedOrigin.equals(candidate);
  }
  private boolean privateIp(String h){
    if(h.equals("127.0.0.1")||h.equals("localhost"))return true;
    if(!h.matches("[0-9.]+"))return false;
    String[] parts=h.split("\\.",-1);
    if(parts.length!=4)return false;
    int[] p=new int[4];
    for(int i=0;i<4;i++){
      if(parts[i].isEmpty()||parts[i].length()>3)return false;
      try{p[i]=Integer.parseInt(parts[i]);}catch(NumberFormatException ex){return false;}
      if(p[i]<0||p[i]>255)return false;
    }
    return p[0]==10||p[0]==127||p[0]==192&&p[1]==168||p[0]==172&&p[1]>=16&&p[1]<=31;
  }
  @Override protected void onActivityResult(int req,int res,Intent i){
    super.onActivityResult(req,res,i);
    if(req==PICK_FILES&&upload!=null){
      upload.onReceiveValue(WebChromeClient.FileChooserParams.parseResult(res,i));
      upload=null;
    }
  }
  @Override public void onBackPressed(){if(browser!=null&&browser.canGoBack())browser.goBack();else super.onBackPressed();}
  @Override protected void onDestroy(){
    if(upload!=null)upload.onReceiveValue(null);
    if(browser!=null)browser.destroy();
    super.onDestroy();
  }
}
