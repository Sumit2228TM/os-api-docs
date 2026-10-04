package com.krishagni.catissueplus.core.query;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;

import com.krishagni.catissueplus.core.common.Pair;
import com.krishagni.catissueplus.core.common.errors.OpenSpecimenException;
import com.krishagni.catissueplus.core.de.services.SavedQueryErrorCode;

import edu.common.dynamicextensions.query.ResultColumn;
import edu.common.dynamicextensions.query.ResultPostProc;
import edu.common.dynamicextensions.query.ResultPostProcFactory;
import edu.common.dynamicextensions.query.RowsList;
import edu.common.dynamicextensions.query.ast.QueryExpressionNode;
import edu.common.dynamicextensions.query.ast.ResultPostProcNode;

public class SpecimenQtyProcFactory implements ResultPostProcFactory {
	@Override
	public ResultPostProc create(QueryExpressionNode queryExpr, String timeZone) {
		ResultPostProcNode procSpec = queryExpr.getResultPostProc();
		List<String> args = procSpec.getArgs();
		validateArgs(args);
		validateQueryExpr(queryExpr, args.get(0));

		final String restrictBy = args.get(0);
		final BigDecimal minQty = StringUtils.isBlank(args.get(1)) ? BigDecimal.ZERO : new BigDecimal(args.get(1));
		final BigDecimal maxQty = StringUtils.isBlank(args.get(2)) ? new BigDecimal("1000000000000000") : new BigDecimal(args.get(2));
		return new ResultPostProc() {
			MathContext mathContext = new MathContext(6, RoundingMode.HALF_UP);

			ResultPostProc defProc;

			Map<String, GroupRows> groupRowsMap = new LinkedHashMap<>();

			RowsList rows;

			@Override
			public int processResultSet(ResultSet rs, ResultPostProc defProc) {
				this.defProc = defProc;
				return defProc.processResultSet(rs, defProc);
			}

			@Override
			public List<ResultColumn> getResultColumns() {
				return defProc.getResultColumns();
			}

			@Override
			public RowsList getRows() {
				int groupIdx = groupIndex();
				int typeIdx  = specimenTypeIndex();
				int qtyIdx   = quantityIndex();

				Iterator<Object[]> iter =  defProc.getRows().iterator();
				int rowIdx = -1;
				while (iter.hasNext()) {
					++rowIdx;
					Object[] row = iter.next();
					Object key = row[groupIdx];
					if (key == null) {
						continue;
					}

					Object type = row[typeIdx];
					if (type == null) {
						continue;
					}

					Object qtyObj = row[qtyIdx];
					if (!(qtyObj instanceof Number)) {
						continue;
					}

					BigDecimal qty = new BigDecimal(((Number) qtyObj).doubleValue());
					if (qty.compareTo(BigDecimal.ZERO) <= 0) {
						continue;
					}

					String grpKey = (key.toString() + "_" + type.toString()).toLowerCase();
					GroupRows grpRows = groupRowsMap.computeIfAbsent(grpKey, (k) -> new GroupRows());
					grpRows.total = grpRows.total.add(qty, mathContext);
					grpRows.quantities.add(Pair.make(rowIdx, qty));
				}

				List<Integer> selectedRows = new ArrayList<>();
				for (GroupRows grpRows : groupRowsMap.values()) {
					if (grpRows.total.compareTo(minQty) < 0) {
						continue;
					}

					if (grpRows.total.compareTo(maxQty) <= 0) {
						selectedRows.addAll(indexes(grpRows.quantities));
						continue;
					}

					List<Pair<Integer, BigDecimal>> ascSelected = selectInAscOrder(grpRows);
					BigDecimal ascTotal = total(ascSelected);

					List<Pair<Integer, BigDecimal>> descSelected = selectInDescOrder(grpRows);
					BigDecimal descTotal = total(descSelected);

					if (ascTotal.compareTo(minQty) >= 0 && ascTotal.compareTo(descTotal) >= 0) {
						selectedRows.addAll(indexes(ascSelected));
					} else if (descTotal.compareTo(minQty) >= 0 && descTotal.compareTo(ascTotal) >= 0) {
						selectedRows.addAll(indexes(descSelected));
					}
				}

				rows = pickSelected(selectedRows);
				return rows;
			}

			@Override
			public void cleanup() {
				if (defProc != null) {
					defProc.cleanup();
				}

				if (rows != null) {
					rows.cleanup();
				}
			}

			private Integer groupIndex() {
				int groupIdx = -1;
				int idx = -1;
				for (ResultColumn column : getResultColumns()) {
					++idx;
					String columnAql = column.getExpression().getAql();
					if (columnAql.equals("Participant.id")) {
						if (groupIdx == -1 && restrictBy.equals("participant")) {
							groupIdx = idx;
							break;
						}
					} else if (columnAql.equals("Specimen.parentSpecimen.parentId")) {
						if (groupIdx == -1 && restrictBy.equals("parent_specimen")) {
							groupIdx = idx;
							break;
						}
					}
				}

				return groupIdx;
			}

			private Integer specimenTypeIndex() {
				int idx = -1;
				for (ResultColumn column : getResultColumns()) {
					++idx;
					if (column.getExpression().getAql().equals("Specimen.type")) {
						return idx;
					}
				}

				return -1;
			}

			private Integer quantityIndex() {
				int idx = -1;
				for (ResultColumn column : getResultColumns()) {
					++idx;
					if (column.getExpression().getAql().equals("Specimen.availableQty")) {
						return idx;
					}
				}

				return -1;
			}

			private List<Pair<Integer, BigDecimal>> selectInAscOrder(GroupRows grpRows) {
				List<Pair<Integer, BigDecimal>> ascQtyList = grpRows.quantities.stream()
					.sorted((q1, q2) -> q1.second().compareTo(q2.second()))
					.collect(Collectors.toList());

				List<Pair<Integer, BigDecimal>> ascSelected = new ArrayList<>();
				BigDecimal ascTotal = BigDecimal.ZERO;
				for (Pair<Integer, BigDecimal> qty : ascQtyList) {
					if (ascTotal.add(qty.second(), mathContext).compareTo(maxQty) > 0) {
						break;
					}


					ascTotal = ascTotal.add(qty.second(), mathContext);
					ascSelected.add(qty);
				}

				return ascSelected;
			}

			private List<Pair<Integer, BigDecimal>> selectInDescOrder(GroupRows grpRows) {
				List<Pair<Integer, BigDecimal>> descQtyList = grpRows.quantities.stream()
					.sorted((q1, q2) -> q2.second().compareTo(q1.second()))
					.collect(Collectors.toList());

				List<Pair<Integer, BigDecimal>> descSelected = new ArrayList<>();
				BigDecimal descTotal = BigDecimal.ZERO;
				for (Pair<Integer, BigDecimal> qty : descQtyList) {
					if (descTotal.add(qty.second(), mathContext).compareTo(maxQty) > 0) {
						continue;
					}


					descTotal = descTotal.add(qty.second(), mathContext);
					descSelected.add(qty);
				}

				return descSelected;
			}

			private List<Integer> indexes(List<Pair<Integer, BigDecimal>> items) {
				return items.stream().map(Pair::first).collect(Collectors.toList());
			}

			private BigDecimal total(List<Pair<Integer, BigDecimal>> items) {
				return items.stream().map(Pair::second).reduce(BigDecimal.ZERO, (a, b) -> a.add(b, mathContext));
			}

			private RowsList pickSelected(List<Integer> selectedRows) {
				selectedRows.sort((idx1, idx2) -> Integer.compare(idx1, idx2));
				RowsList result = new RowsList();
				try {
					int idx = -1;
					Iterator<Object[]> iter = defProc.getRows().iterator();
					for (int selectedRow : selectedRows) {
						while (iter.hasNext()) {
							++idx;
							Object[] row = iter.next();
							if (idx == selectedRow) {
								result.add(row);
								break;
							}
						}
					}
				} catch (Exception e) {
					result.cleanup();
					throw e;
				}

				return result;
			}

			class GroupRows {
				boolean qualified = false;

				List<Pair<Integer, BigDecimal>> quantities = new ArrayList<>();

				BigDecimal total = BigDecimal.ZERO;
			}
		};
	}

	private void validateArgs(List<String> args) {
		if (args == null || args.size() < 3) {
			throw OpenSpecimenException.userError(
				SavedQueryErrorCode.MALFORMED,
				"Specimen quantity restriction args not provided. The format is specimenqty(group, min, max)");
		}

		if (args.get(0) == null) {
			throw OpenSpecimenException.userError(
				SavedQueryErrorCode.MALFORMED,
				"Specimen quantity restriction group not specified. Allowed values are participant, parent_specimen");
		}

		final String restrictBy = args.get(0);
		if (!(restrictBy.equals("participant") || restrictBy.equals("parent_specimen"))) {
			throw OpenSpecimenException.userError(
				SavedQueryErrorCode.MALFORMED,
				"Invalid specimen quantity restriction group: " + restrictBy + ". Allowed values are participant, parent_specimen");
		}

		BigDecimal minQty = null;
		try {
			if (StringUtils.isBlank(args.get(1))) {
				minQty = new BigDecimal(0);
			} else {
				minQty = new BigDecimal(args.get(1));
			}
		} catch (NumberFormatException nfe) {
			throw OpenSpecimenException.userError(
				SavedQueryErrorCode.MALFORMED,
				"Invalid min. quantity: " + args.get(1));
		}

		BigDecimal maxQty = null;
		try {
			if (StringUtils.isBlank(args.get(2))) {
				maxQty = new BigDecimal("1000000000000000");
			} else {
				maxQty = new BigDecimal(args.get(2));
			}
		} catch (NumberFormatException nfe) {
			throw OpenSpecimenException.userError(
				SavedQueryErrorCode.MALFORMED,
				"Invalid max. quantity: " + args.get(2));
		}

		if (minQty.compareTo(maxQty) > 0) {
			throw OpenSpecimenException.userError(
				SavedQueryErrorCode.MALFORMED,
				"Min. quantity cannot be more than max. quantity!");
		}
	}

	private void validateQueryExpr(QueryExpressionNode queryExpr, String restrictBy) {
		boolean hasQtyField = queryExpr.getSelectList().getElements().stream()
			.anyMatch(expr -> "Specimen.availableQty".equals(expr.getAql()));
		if (!hasQtyField) {
			throw OpenSpecimenException.userError(
				SavedQueryErrorCode.MALFORMED,
				"Specimen available quantity field is not selected in the results view");
		}

		boolean hasTypeField = queryExpr.getSelectList().getElements().stream()
			.anyMatch(expr -> "Specimen.type".equals(expr.getAql()));
		if (!hasTypeField) {
			throw OpenSpecimenException.userError(
				SavedQueryErrorCode.MALFORMED,
				"Specimen type field is not selected in the results view");
		}

		if (restrictBy.equals("participant")) {
			boolean hasParticipantId = queryExpr.getSelectList().getElements().stream()
				.anyMatch(expr -> "Participant.id".equals(expr.getAql()) && !"$cprId".equals(expr.getLabel()));
			if (!hasParticipantId) {
				throw OpenSpecimenException.userError(
					SavedQueryErrorCode.MALFORMED,
					"Registration ID field is not selected in the results view");
			}
		} else if (restrictBy.equals("parent_specimen")) {
			boolean hasParentId = queryExpr.getSelectList().getElements().stream()
				.anyMatch(expr -> "Specimen.parentSpecimen.parentId".equals(expr.getAql()));
			if (!hasParentId) {
				throw OpenSpecimenException.userError(
					SavedQueryErrorCode.MALFORMED,
					"Specimen parent ID field is not selected in the results view");
			}
		}
	}
}
