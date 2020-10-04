package Mentalist.utils;
/*
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.gdata.client.spreadsheet.SpreadsheetQuery;
import com.google.gdata.client.spreadsheet.SpreadsheetService;
import com.google.gdata.client.spreadsheet.WorksheetQuery;
import com.google.gdata.data.PlainTextConstruct;
import com.google.gdata.data.spreadsheet.*;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.*;

public class GoogleSpreadSheetUtils {
    // アプリケーション名 (任意)
    private static final String APPLICATION_NAME = "Mentalist";

    // アカウント
    private static final String ACCOUNT_P12_ID = "experiment@mentalist-291505.iam.gserviceaccount.com";
    private static final File P12FILE = new File("mentalist-291505-44cc6f4fb881.p12");

    // 認証スコープ
    private static final List<String> SCOPES = Arrays.asList(
            "https://docs.google.com/feeds",
            "https://spreadsheets.google.com/feeds");

    // Spreadsheet API URL
    private static final String SPREADSHEET_URL = "https://spreadsheets.google.com/feeds/spreadsheets/private/full";

    private static final URL SPREADSHEET_FEED_URL;

    static {
        try {
            SPREADSHEET_FEED_URL = new URL(SPREADSHEET_URL);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    private static Credential authorize() throws Exception {
        System.out.println("authorize in");

        HttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
        JsonFactory jsonFactory = new JacksonFactory();

        GoogleCredential credential = new GoogleCredential.Builder()
                .setTransport(httpTransport)
                .setJsonFactory(jsonFactory)
                .setServiceAccountId(ACCOUNT_P12_ID)
                .setServiceAccountPrivateKeyFromP12File(P12FILE)
                .setServiceAccountScopes(SCOPES)
                .build();

        boolean ret = credential.refreshToken();
        // debug dump
        System.out.println("refreshToken:" + ret);

        // debug dump
        if (credential != null) {
            System.out.println("AccessToken:" + credential.getAccessToken());
        }

        System.out.println("authorize out");

        return credential;
    }

    private static SpreadsheetService getService() throws Exception {
        System.out.println("service in");

        SpreadsheetService service = new SpreadsheetService(APPLICATION_NAME);
        service.setProtocolVersion(SpreadsheetService.Versions.V3);

        Credential credential = authorize();
        service.setOAuth2Credentials(credential);

        // debug dump
        System.out.println("Schema: " + service.getSchema().toString());
        System.out.println("Protocol: " + service.getProtocolVersion().getVersionString());
        System.out.println("ServiceVersion: " + service.getServiceVersion());

        System.out.println("service out");

        return service;
    }

    private static List<SpreadsheetEntry> findAllSpreadsheets(SpreadsheetService service) throws Exception {
        System.out.println("findAllSpreadsheets in");

        SpreadsheetFeed feed = service.getFeed(SPREADSHEET_FEED_URL, SpreadsheetFeed.class);

        List<SpreadsheetEntry> spreadsheets = feed.getEntries();

        // debug dump
        for (SpreadsheetEntry spreadsheet : spreadsheets) {
            System.out.println("title: " + spreadsheet.getTitle().getPlainText());
        }

        System.out.println("findAllSpreadsheets out");
        return spreadsheets;
    }

    private static SpreadsheetEntry findSpreadsheetByName(SpreadsheetService service, String spreadsheetName) throws Exception {
        System.out.println("findSpreadsheetByName in");
        SpreadsheetQuery sheetQuery = new SpreadsheetQuery(SPREADSHEET_FEED_URL);
        sheetQuery.setTitleQuery(spreadsheetName);
        SpreadsheetFeed feed = service.query(sheetQuery, SpreadsheetFeed.class);
        SpreadsheetEntry ssEntry = null;
        if (feed.getEntries().size() > 0) {
            ssEntry = feed.getEntries().get(0);
        }
        System.out.println("findSpreadsheetByName out");
        return ssEntry;
    }

    private static WorksheetEntry findWorksheetByName(SpreadsheetService service, SpreadsheetEntry ssEntry, String sheetName) throws Exception {
        System.out.println("findWorksheetByName in");
        WorksheetQuery worksheetQuery = new WorksheetQuery(ssEntry.getWorksheetFeedUrl());
        worksheetQuery.setTitleQuery(sheetName);
        WorksheetFeed feed = service.query(worksheetQuery, WorksheetFeed.class);
        WorksheetEntry wsEntry = null;
        if (feed.getEntries().size() > 0){
            wsEntry = feed.getEntries().get(0);
        }
        System.out.println("findWorksheetByName out");
        return wsEntry;
    }

    private static WorksheetEntry addWorksheet(SpreadsheetService service, SpreadsheetEntry ssEntry, String sheetName, int colNum, int rowNum) throws Exception {
        System.out.println("addWorksheet in");
        WorksheetEntry wsEntry = new WorksheetEntry();
        wsEntry.setTitle(new PlainTextConstruct(sheetName));
        wsEntry.setColCount(colNum);
        wsEntry.setRowCount(rowNum);
        URL worksheetFeedURL = ssEntry.getWorksheetFeedUrl();
        System.out.println("addWorksheet out");
        return service.insert(worksheetFeedURL, wsEntry);
    }

    private static void deleteWorksheet(WorksheetEntry wsEntry) throws Exception {
        System.out.println("deleteWorksheet in");
        wsEntry.delete();
        System.out.println("deleteWorksheet out");
    }

    private static void insertHeadRow(SpreadsheetService service, WorksheetEntry wsEntry, List<String> header, String query) throws Exception {
        System.out.println("insertHeadRow in");

        URL cellFeedUrl = new URI(wsEntry.getCellFeedUrl().toString() + query).toURL();
        CellFeed cellFeed = service.getFeed(cellFeedUrl, CellFeed.class);

        for (int i=0; i<header.size(); i++) {
            cellFeed.insert(new CellEntry(1, i+1, header.get(i)));
        }

        System.out.println("insertHeadRow out");
    }

    private static void insertDataRow(SpreadsheetService service, WorksheetEntry wsEntry, Map<String, Object> values) throws Exception {
        System.out.println("insertDataRow in");

        ListEntry dataRow = new ListEntry();

        values.forEach((title,value)->{
            dataRow.getCustomElements().setValueLocal(title, value.toString());
        });

        URL listFeedUrl = wsEntry.getListFeedUrl();
        service.insert(listFeedUrl, dataRow);

        System.out.println("insertDataRow out");
    }

    private static void updateDataRow(SpreadsheetService service, WorksheetEntry wsEntry, int rowNum, Map<String, Object> values) throws Exception {
        System.out.println("updateDataRow in");

        URL listFeedUrl = wsEntry.getListFeedUrl();
        ListFeed listFeed = service.getFeed(listFeedUrl, ListFeed.class);

        ListEntry row = listFeed.getEntries().get(rowNum);

        values.forEach((title,value)->{
            row.getCustomElements().setValueLocal(title, value.toString());
        });

        row.update();

        System.out.println("updateDataRow out");
    }

    private static String makeQuery(int minrow, int maxrow, int mincol, int maxcol) {
        String base = "?min-row=MINROW&max-row=MAXROW&min-col=MINCOL&max-col=MAXCOL";
        return base.replaceAll("MINROW", String.valueOf(minrow))
                .replaceAll("MAXROW", String.valueOf(maxrow))
                .replaceAll("MINCOL", String.valueOf(mincol))
                .replaceAll("MAXCOL", String.valueOf(maxcol));
    }

    public static void test(){
        System.out.println("main start");

        try {
            SpreadsheetService service = getService();

            findAllSpreadsheets(service);

            String ssName = "NegotiationResult";

            SpreadsheetEntry ssEntry = findSpreadsheetByName(service, ssName);

            String wsName = "テストシート";

            WorksheetEntry wsEntry = findWorksheetByName(service, ssEntry, wsName);
            if (wsEntry != null) {
                deleteWorksheet(wsEntry);
            }

            WorksheetEntry newWorksheet = addWorksheet(service, ssEntry, wsName, 50, 100);

            // ワークシートのタイトル名
            List<String> header = new ArrayList<>();
            header.add("test1");
            header.add("test2");
            header.add("test3");
            header.add("test4");
            header.add("test5");
            header.add("test6");

            insertHeadRow(service, newWorksheet, header, makeQuery(1, 1, 1, 5));

            // debug dump
            //specCell(service, newWorksheet, makeQuery(1, 1, 1, 5));


            // insert
            Map<String, Object> insertValues1 = new HashMap<>();
            insertValues1.put("test1", "2015-09-01");
            insertValues1.put("test2", 1200);
            insertValues1.put("test3", 1300);
            insertValues1.put("test4", 1400);
            insertValues1.put("test5", 1500);
            insertValues1.put("test6", 1600);

            insertDataRow(service, newWorksheet, insertValues1);


            // insert
            Map<String, Object> insertValues2 = new HashMap<>();
            insertValues2.put("test1", "2015-09-02");
            insertValues2.put("test2", 2200);
            insertValues2.put("test3", 2300);
            insertValues2.put("test4", 2400);
            insertValues2.put("test5", 2500);
            insertValues2.put("test6", 2600);

            insertDataRow(service, newWorksheet, insertValues2);


            // update
            Map<String, Object> updateValues = new HashMap<>();
            updateValues.put("test1", "2015-09-01");
            updateValues.put("test2", 1202);
            updateValues.put("test3", 1303);
            updateValues.put("test4", 1404);
            updateValues.put("test5", 1505);
            updateValues.put("test6", 1606);

            updateDataRow(service, newWorksheet, 0, updateValues);
        } catch (Exception e){
            e.printStackTrace();
        }

        System.out.println("main end");
    }
}
*/

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.SheetsScopes;
import com.google.api.services.sheets.v4.model.ValueRange;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.List;

public class GoogleSpreadSheetUtils{
    private static final String APPLICATION_NAME = "Google Sheets API Java Quickstart";
    private static JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
    private static final String TOKENS_DIRECTORY_PATH = "tokens";

    /**
     * Global instance of the scopes required by this quickstart.
     * If modifying these scopes, delete your previously saved tokens/ folder.
     */
    private static final List<String> SCOPES = Collections.singletonList(SheetsScopes.SPREADSHEETS_READONLY);
    private static final String CREDENTIALS_FILE_PATH = "client_secret.json";

    /**
     * Creates an authorized Credential object.
     * @param HTTP_TRANSPORT The network HTTP Transport.
     * @return An authorized Credential object.
     * @throws IOException If the credentials.json file cannot be found.
     */
    private static Credential getCredentials(final NetHttpTransport HTTP_TRANSPORT) throws IOException {
        // Load client secrets.
        InputStream in = GoogleSpreadSheetUtils.class.getResourceAsStream(CREDENTIALS_FILE_PATH);
        if (in == null) {
            throw new FileNotFoundException("Resource not found: " + CREDENTIALS_FILE_PATH);
        }
        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

        // Build flow and trigger user authorization request.
        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
                .setDataStoreFactory(new FileDataStoreFactory(new java.io.File(TOKENS_DIRECTORY_PATH)))
                .setAccessType("offline")
                .build();
        LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8888).build();
        return new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");
    }

    /**
     * Prints the names and majors of students in a sample spreadsheet:
     * https://docs.google.com/spreadsheets/d/1BxiMVs0XRA5nFMdKvBdBZjgmUUqptlbs74OgvE2upms/edit
     */
    public static void test() throws IOException, GeneralSecurityException {
        // Build a new authorized API client service.
        final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        final String spreadsheetId = "1BxiMVs0XRA5nFMdKvBdBZjgmUUqptlbs74OgvE2upms";
        final String range = "Class Data!A2:E";
        Sheets service = new Sheets.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
                .setApplicationName(APPLICATION_NAME)
                .build();
        ValueRange response = service.spreadsheets().values()
                .get(spreadsheetId, range)
                .execute();
        List<List<Object>> values = response.getValues();
        if (values == null || values.isEmpty()) {
            System.out.println("No data found.");
        } else {
            System.out.println("Name, Major");
            for (List row : values) {
                // Print columns A and E, which correspond to indices 0 and 4.
                System.out.printf("%s, %s\n", row.get(0), row.get(4));
            }
        }
    }

}