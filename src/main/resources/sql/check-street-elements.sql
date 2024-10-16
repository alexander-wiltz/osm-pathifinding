SELECT s.id, s.name, s.house_number, n.lon, n.lat FROM streets s
JOIN public.street_node_relation snr on s.id = snr.street_id
JOIN public.nodes n on n.id = snr.node_id
WHERE s.name = 'Brückenstraße' AND s.is_building = false ;

SELECT s.* FROM streets s
WHERE s.name = 'Brückenstraße';
SELECT w.* FROM ways w
WHERE w.name = 'Brückenstraße' AND w.is_building = false;

SELECT DISTINCT n.id, w.name, n.lat, n.lon FROM ways w
JOIN public.node_way_relation nwr on w.id = nwr.way_id
JOIN public.nodes n on nwr.node_id = n.id
WHERE w.name = 'Brückenstraße' AND w.is_building = false ORDER BY n.id;