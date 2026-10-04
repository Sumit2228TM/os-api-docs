package com.krishagni.catissueplus.core.init;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import krishagni.catissueplus.beans.FormContextBean;

public class ImportUserForms extends ImportForms implements InitializingBean {
	private static final String USER_FORMS = "User";

	public boolean isCreateTable() {
		return true;
	}

	@Override
	public void afterPropertiesSet()
	throws Exception {
		setCreateTable(true);
		super.afterPropertiesSet();
	}

	@Override
	protected Collection<String> listFormFiles() throws IOException {
		List<String> files = new ArrayList<>();

		try {
			PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver(getClass().getClassLoader());
			Resource[] resources = resolver.getResources("classpath*:/user-forms/*.xml");
			for (Resource resource: resources) {
				String filename = resource.getFilename();
				files.add("/user-forms/" + filename);
			}
		} catch (FileNotFoundException e) {

		}

		return files;
	}

	@Override
	protected boolean isSysForm(String formFile) {
		return false;
	}

	@Override
	protected FormContextBean getFormContext(String formFile, Long formId) {
		FormContextBean fc = getDaoFactory().getFormDao().getFormContext(formId, -1L, USER_FORMS);
		if (fc == null) {
			fc = new FormContextBean();
		}

		fc.setContainerId(formId);
		fc.setCpId(-1L);
		fc.setEntityType(USER_FORMS);
		fc.setEntityId(-1L);
		fc.setMultiRecord(true);
		fc.setSortOrder(null);
		fc.setSysForm(isSysForm(formFile));
		return fc;
	}

	@Override
	protected void cleanup() {
	}
}
