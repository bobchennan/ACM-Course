package cnx.translate;

import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import cnx.quad.*;
import cnx.temp.Temp;
import cnx.translate.CompilationUnit;

public class DeadCodeEliminator {

	public void eliminate(CompilationUnit u) {
		List<Quad> result = new LinkedList<Quad>();
		for (Quad q : u.getQuads()) {
			if (q instanceof Binop  || q instanceof Load  || q instanceof Move || q instanceof Unary) {
				Set<Temp> x = new LinkedHashSet<Temp>(q.def());
				x.retainAll(q.OUT);
				if (x.size() == 0) {
//					System.out.println("\t\t\t" + q);
				}
				else {
					result.add(q);
				}
			}
			else {
				result.add(q);
			}
		}
		u.setQuads(result);
	}
}
