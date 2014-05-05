package tw.com.mds.fet.femtocellportal.web;

public class MainAction extends IdentityAction {

	private static final long serialVersionUID = 1L;

	private boolean hideMenuAnnouncement;

	@Override
	public void prepare() throws Exception {
		hideMenuAnnouncement = true;
		super.prepare();
	}

	public String main() {
		return SUCCESS;
	}

	public boolean isHideMenuAnnouncement() {
		return hideMenuAnnouncement;
	}

	public void setHideMenuAnnouncement(boolean hideMenuAnnouncement) {
		this.hideMenuAnnouncement = hideMenuAnnouncement;
	}

}
