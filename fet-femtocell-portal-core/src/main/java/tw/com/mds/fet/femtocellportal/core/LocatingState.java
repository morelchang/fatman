package tw.com.mds.fet.femtocellportal.core;

public enum LocatingState {
	
	// 0:無法定位
	UNABLE_TO_LOCATE,
	// 1:縣市定位
	LOCATED_BY_CITY,
	// 2:鄉鎮定位
	LOCATED_BY_TOWN,
	// 3:路名不全
	STREET_UNRECOGNIZED,
	// 4:小過最小門牌取最小值或路名定位
	LOCATED_BY_STREET,
	// 5:超過最大門牌取最大值
	LOCATED_BY_MAX_HOUSENUMBER,
	// 6:內插或內擴
	LOCATED_BY_INTERPOLATION_EXPANSION,
	// 7:區門牌修正
	LOCATED_BY_FIXED_HOUSENUMBER,
	// 8:無鄉鎮絕對定位
	LOCATED_ABSOLUTELY_WITHOUT_TOWN,
	// 9:絕對定位
	LOCATED_ABSOLUTELY
	
}
