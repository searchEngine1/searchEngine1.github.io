package il.ac.shenkar.common;


public interface IAppConstants {
	
	public static final String 	DB_MYSQL= "il.ac.shenkar.model.dao.mysql.resources.db";
	public static final String 	RES_KEY_DATASOURCE= "datasource";
	public static final String 	RESOURCE_BUNDLE= "il.ac.shenkar.nls.resources.SearchEngine_iw";
	
	/* select keys */

	public static final String RES_KEY_SELECT_ONE_WORD = "select.one.word";
	public static final String RES_KEY_SELECT_DOCS_BY_ID = "select.docs.by.id";
	public static final String RES_KEY_SELECT_SYNONYMS= "select.synonyms";
	public static final String RES_KEY_SELECT_DOCS = "select.docs.enable";
	public static final String RES_KEY_SELECT_DOCS_DISABLED = "select.docs.disabled";
	public static final String RES_KEY_SELECT_OBJECT_WORD = "select.object.word";
	public static final String RES_KEY_SELECT_JOKER_WORD = "select.joker.word";
	public static final String RES_KEY_VERIFY_STOP_LIST = "select.verify.stop.list";

	/* count keys */
	
	public static final String RES_KEY_COUNT_OCCURENCE_IN_STOP_LIST = "count.show.by.word";
	public static final String RES_KEY_COUNT_DOCS_INDEXES = "count.docs.indexes";
	
	/* insert keys */

	public static final String RES_KEY_INSERT_DOC_DETAILS = "insert.doc.details";
	public static final String RES_KEY_INSERT_TO_INDEX_TABLE = "insert.index.table";
	
	/* update keys */
	
	public static final String RES_KEY_UPDATE_SUMMARY = "update.summary";
	public static final String RES_KEY_UPDATE_DISABLE_DOC = "update.disable.doc";
	public static final String RES_KEY_UPDATE_ENABLE_DOC = "update.enable.doc";
	public static final String RES_KEY_UPDATE_DOCS_FOR_EXISTING_WORD = "update.docs.for.existing.word";
	
	/* delete keys*/

	
	/* Path constants */
	public static final String 	HOME_PAGE_RELATIVE_PATH = "/jsp/homepage.jsp";
	public static final String LOGIN_PAGE_RELATIVE_PATH = "/jsp/login.jsp";
	public static final String HOME_RELATIVE_PATH = "/jsp/home.jsp";
	public static final String ADMIN_RELATIVE_PATH = "/jsp/admin.jsp";
	
	/* sort key */
	public static final String  RES_KEY_SORT_DOCS_INDEXES = "sort.dos.indexes.table";


}
