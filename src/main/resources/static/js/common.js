function createNamespace(ns) {
	let parent = window;
	ns.split('.').forEach(function(item, idx) {
		parent[item] = {};
		parent = parent[item];
	});
}
