create database themart;
use themart;
show tables;

select * from users;
    SET FOREIGN_KEY_CHECKS = 0;
truncate users;
    SET FOREIGN_KEY_CHECKS = 1;
    
    ALTER TABLE users MODIFY password VARCHAR(255) NULL;

update users set id =3 where id=9;
delete  from users where id=11;

UPDATE users SET role = 'ADMIN' WHERE id = 2;

INSERT INTO blogs (title, category, author, image_url, tags, content, is_published, created_at) VALUES

('How To Style Winter Whites',
 'Style',
 'Admin',
 '/images/blog1.jpg',
 'Fashion, Winter, Style, Outfit',
 '<p>Winter whites are one of the most elegant and timeless looks you can wear during the cold months. Gone are the days when wearing white after Labor Day was considered a fashion faux pas.</p><p>Start with a cream or ivory base — think wide-leg trousers or a flowing midi skirt. Layer with a chunky knit sweater in off-white or ecru. The key is to mix textures: wool, cashmere, and cotton all play well together in a tonal white palette.</p><p>Finish the look with nude or tan accessories to keep the palette clean, or go bold with a camel coat for contrast. Winter whites work beautifully for both casual days and dressed-up evenings.</p>',
 true,
 NOW() - INTERVAL 1 DAY),

('Top 10 Wardrobe Essentials for Every Woman',
 'Style',
 'Admin',
 '/images/blog2.jpg',
 'Wardrobe, Essentials, Fashion, Women',
 '<p>Building a capsule wardrobe is one of the best investments you can make for your personal style. Instead of owning hundreds of pieces you rarely wear, a curated collection of essentials gives you endless outfit possibilities.</p><p>The must-haves include: a well-fitted white shirt, a classic pair of straight-leg jeans, a tailored blazer, a little black dress, a trench coat, quality white sneakers, ankle boots, a silk blouse, high-waisted trousers, and a versatile midi skirt.</p><p>Each of these pieces works on its own and pairs effortlessly with the others. Invest in quality over quantity — well-made basics last years and always look polished.</p>',
 true,
 NOW() - INTERVAL 2 DAY),

('The Rise of Sustainable Fashion in 2024',
 'Transparency',
 'Admin',
 '/images/blog3.jpg',
 'Sustainable, Eco, Fashion, Environment',
 '<p>Sustainable fashion is no longer a niche movement — it has become a mainstream priority for both brands and consumers. In 2024, more shoppers than ever are asking where their clothes come from and how they are made.</p><p>Brands are responding by adopting organic materials, reducing water usage, eliminating toxic dyes, and committing to fair wages for garment workers. Secondhand and resale platforms have also seen explosive growth as consumers look for ways to refresh their wardrobes without contributing to textile waste.</p><p>Making sustainable choices does not mean sacrificing style. Look for certifications like GOTS, Fair Trade, and B Corp when shopping, and prioritize brands that publish their supply chain information openly.</p>',
 true,
 NOW() - INTERVAL 3 DAY),

('How to Care for Your Cotton Clothes',
 'Tips',
 'Admin',
 '/images/blog4.jpg',
 'Cotton, Care, Laundry, Tips',
 '<p>Cotton is one of the most beloved fabrics in fashion — breathable, soft, and versatile. But without proper care, cotton garments can shrink, fade, or lose their shape quickly.</p><p>Always wash cotton in cold water to prevent shrinkage. Use a gentle detergent and avoid bleach unless the item is white and specifically bleach-safe. Turn dark cotton items inside out before washing to preserve colour.</p><p>Air drying is always the best option for cotton. If you must use a dryer, choose a low heat setting. Iron cotton while it is still slightly damp for the best results, and store folded rather than hung to avoid stretching.</p>',
 true,
 NOW() - INTERVAL 4 DAY),

('Men''s Style Guide: Dressing Smart Casual',
 'Style',
 'Admin',
 '/images/blog5.jpg',
 'Men, Smart Casual, Style, Fashion',
 '<p>Smart casual is one of the most versatile dress codes — polished enough for a business lunch, relaxed enough for a weekend outing. The challenge is striking the right balance between formal and casual elements.</p><p>Start with well-fitted chinos in neutral tones like navy, beige, or olive. Pair them with a crisp Oxford shirt — tucked in for a sharper look or half-tucked for something more relaxed. A clean pair of leather loafers or white sneakers completes the foundation.</p><p>Layer with a structured blazer when you need to elevate the look. Avoid overly distressed denim or graphic tees — smart casual should always look intentional and put-together.</p>',
 true,
 NOW() - INTERVAL 5 DAY),

('Trending Colors of the Season',
 'Trends',
 'Admin',
 '/images/blog6.jpg',
 'Color, Trends, Fashion, Season',
 '<p>Every season brings a fresh palette, and this one is no different. The trending colours this season are rooted in nature — warm terracottas, deep forest greens, soft lavenders, and rich burgundies are dominating runways and street style alike.</p><p>Terracotta is particularly versatile — it works as a top, trouser, or accessory colour and pairs beautifully with cream, white, and chocolate brown. Forest green feels luxurious in velvet or satin textures and looks stunning against warm skin tones.</p><p>If you prefer something lighter, dusty lavender is having a major moment. Try it in a flowy midi dress or a relaxed linen co-ord set for a dreamy, effortless look.</p>',
 true,
 NOW() - INTERVAL 6 DAY),

('A Complete Guide to Layering Outfits',
 'Style',
 'Admin',
 '/images/blog7.jpg',
 'Layering, Winter, Outfit, Tips',
 '<p>Layering is an art form that separates a good outfit from a great one. When done well, it adds depth, dimension, and personality to your look while keeping you warm and comfortable.</p><p>The golden rule of layering is to start thin and build up — a fitted base layer, a mid-weight piece like a shirt or light knit, and a structured outer layer such as a coat or jacket. Vary the lengths of each layer so all pieces are visible and contribute to the overall look.</p><p>Play with textures and tones rather than matching everything perfectly. A denim shirt under a wool blazer under a leather jacket is a classic combination that never goes out of style. Accessories like scarves and belts help define the silhouette within layered outfits.</p>',
 true,
 NOW() - INTERVAL 7 DAY),

('The Power of Accessories: Elevate Any Outfit',
 'Style',
 'Admin',
 '/images/blog8.jpg',
 'Accessories, Style, Jewelry, Bags',
 '<p>Accessories are the finishing touch that transform an ordinary outfit into something memorable. A simple jeans-and-tee combination becomes a statement look with the right belt, bag, and jewellery.</p><p>When it comes to jewellery, the current trend leans toward stacking — multiple thin rings, layered necklaces of different lengths, and ear stacks mixing studs with hoops. The effect is effortlessly curated without looking overdone.</p><p>Bags are equally powerful. A structured tote communicates polish and professionalism, while a crossbody bag adds a casual, hands-free ease to weekend looks. Invest in one or two quality bags in neutral tones and they will work with everything in your wardrobe.</p>',
 true,
 NOW() - INTERVAL 8 DAY),

('Understanding Fabric: What to Wear in Summer',
 'Tips',
 'Admin',
 '/images/blog9.jpg',
 'Fabric, Summer, Cotton, Linen, Tips',
 '<p>Choosing the right fabric in summer makes the difference between staying cool and feeling miserable. The best summer fabrics are natural, breathable, and moisture-wicking.</p><p>Linen is the gold standard for hot weather — it is lightweight, highly breathable, and gets softer with every wash. The natural wrinkle is part of its charm. Cotton is equally excellent, especially in lighter weaves like chambray or poplin.</p><p>Avoid synthetic fabrics like polyester and nylon in intense heat — they trap moisture and heat against the skin. If you must wear synthetics, look for moisture-wicking athletic fabrics specifically designed for warm conditions. Loosely woven and light-coloured garments always perform best in summer heat.</p>',
 true,
 NOW() - INTERVAL 9 DAY),

('How to Build a Minimalist Wardrobe',
 'Tips',
 'Admin',
 '/images/blog10.jpg',
 'Minimalist, Wardrobe, Capsule, Style',
 '<p>A minimalist wardrobe is not about owning as few clothes as possible — it is about owning exactly what you need and nothing more. Every piece earns its place by being versatile, well-made, and genuinely loved.</p><p>Start by decluttering ruthlessly. If you have not worn something in a year, it is time to let it go. Donate, sell, or recycle — but clear the space. Then take stock of what remains and identify gaps rather than impulse-buying trends.</p><p>Build around a neutral base of black, white, grey, and navy. These colours mix and match effortlessly. Add one or two accent colours that genuinely suit you. From there, every addition to your wardrobe should be intentional — bought to fill a specific gap, not out of boredom or impulse.</p>',
 true,
 NOW() - INTERVAL 10 DAY);
 
 
 INSERT INTO categories (name, slug, description, is_active) VALUES
('Desktop', 'desktop', 'Desktop computers and accessories', 1),
('Smart Watch', 'smart-watch', 'Smartwatches and wearables', 1),
('Laptop', 'laptop', 'Laptops and notebooks', 1),
('Smart TV', 'smart-tv', 'Smart televisions and displays', 1),
('Mobile Phone', 'mobile-phone', 'Smartphones and mobile devices', 1),
('Keyboard', 'keyboard', 'Keyboards and input devices', 1),
('Tablet', 'tablet', 'Tablets and e-readers', 1),
('Mouse', 'mouse', 'Computer mice and trackpads', 1),
('Headphone', 'headphone', 'Headphones and earphones', 1),
('Camera', 'camera', 'Cameras and photography equipment', 1);


 truncate blogs;
select * from blogs;
select * from categories;
UPDATE categories SET image_url = NULL WHERE id BETWEEN 3 AND 12;

UPDATE categories
SET name = LOWER(name);

INSERT INTO categories (name, slug, is_active) VALUES

-- Perfume subcategories
('clothes-perfume', 'clothes-perfume', 1),
('deodorant', 'deodorant', 1),
('flower-fragrance', 'flower-fragrance', 1),
('air-freshener', 'air-freshener', 1),

-- Jewellery subcategories
('earrings', 'earrings', 1),
('couple-rings', 'couple-rings', 1),
('necklace', 'necklace', 1),
('bracelets', 'bracelets', 1),

-- Men's
('men''s-formal', 'mens-formal', 1),
('men''s-casual', 'mens-casual', 1),
('sunglasses', 'sunglasses', 1),

-- Women's
('women''s-formal', 'womens-formal', 1),
('women''s-casual', 'womens-casual', 1),
('cosmetics', 'cosmetics', 1),
('perfume', 'perfume', 1),
('bags', 'bags', 1),

-- Others
('shorts-&-jeans', 'shorts-jeans', 1),
('wallet', 'wallet', 1),
('makeup-kit', 'makeupkit', 1);




select * from blogs;
select * from categories;
select * from products;



UPDATE categories SET slug = 'shirt'      WHERE name LIKE '%Shirt%';
UPDATE categories SET slug = 'jacket'     WHERE name LIKE '%Jacket%';
UPDATE categories SET slug = 'skirt'      WHERE name LIKE '%Skirt%';
UPDATE categories SET slug = 'casual'     WHERE name LIKE '%Casual%';
UPDATE categories SET slug = 'watches'    WHERE name LIKE '%Watch%';
UPDATE categories SET slug = 'party-wear' WHERE name LIKE '%Party%';
UPDATE categories SET slug = 'sports-shoe'WHERE name LIKE '%Sports Shoe%';
UPDATE categories SET slug = 'formal'     WHERE name LIKE '%Formal%';
UPDATE categories SET slug = 'sports-wear'WHERE name LIKE '%Sports Wear%';
UPDATE categories SET slug = 'desktop'    WHERE name LIKE '%Desktop%';
UPDATE categories SET slug = 'smart-watch'WHERE name LIKE '%Smart Watch%';


-- Men's dropdown
UPDATE categories SET slug = 'shirts'       WHERE name = 'Men''s Shirt';
UPDATE categories SET slug = 'shorts-jeans' WHERE name = 'Men''s Casual Shoe';
UPDATE categories SET slug = 'shoes'        WHERE name = 'Men''s Formal Shoe';
UPDATE categories SET slug = 'wallet'       WHERE name = 'Watches';

-- Women's dropdown
UPDATE categories SET slug = 'dress-frock'  WHERE name = 'Women''s Skirt';
UPDATE categories SET slug = 'earrings'     WHERE name = 'Women''s Party Wear';
UPDATE categories SET slug = 'sports-wear'  WHERE name = 'Sports Wear';
UPDATE categories SET slug = 'makeup-kit'   WHERE name = 'Sports Shoe';


INSERT INTO products (
    name, short_description, description, price, original_price,
    stock_quantity, image_url, category_id, is_organic, is_featured,
    is_active, discount_percentage, unit, rating, tags
)
VALUES
(
    'FreshGuard Active Deodorant',
    '48h sweat protection',
    'High-performance deodorant with 48-hour odor control, anti-bacterial shield, and quick-dry formula. Suitable for active lifestyles and intense workouts.',
    35.00, 50.00, 50, '/images/products/deodorant-1.png',
    23, false, false, true, 30, 'piece', 4.5,
    'deodorant,active,fresh,protection'
),
(
    'Cool Breeze Roll-On',
    'Refreshing daily defense',
    'Roll-on deodorant enriched with cooling menthol extract, aluminum-free formula, and long-lasting freshness for everyday use.',
    42.00, 60.00, 42, '/images/products/deodorant-2.png',
    23, false, false, true, 30, 'piece', 4.6,
    'deodorant,rollon,cool,daily'
),
(
    'Herbal Shield Spray',
    'Natural odor protection',
    'Herbal-based spray deodorant featuring tea tree and aloe vera extracts. Provides skin-friendly odor control with soothing properties.',
    55.00, 78.00, 35, '/images/products/deodorant-3.png',
    23, false, false, true, 29, 'piece', 4.7,
    'deodorant,herbal,spray,natural'
),
(
    'Urban Musk Deo',
    'Bold masculine fragrance',
    'Premium deodorant with deep musk aroma, sweat-resistant technology, and skin-conditioning ingredients for all-day confidence.',
    48.00, 68.00, 40, '/images/products/deodorant-4.png',
    23, false, false, true, 29, 'piece', 4.5,
    'deodorant,musk,premium,men'
),
(
    'Citrus Energy Stick',
    'Zesty freshness boost',
    'Deodorant stick infused with citrus oils, fast-absorbing texture, and 24-hour freshness. Designed for travel and on-the-go use.',
    60.00, 85.00, 30, '/images/products/deodorant-5.png',
    23, false, false, true, 29, 'piece', 4.8,
    'deodorant,citrus,stick,travel'
),
(
    'Sensitive Care Deo',
    'Gentle skin formula',
    'Dermatologically tested deodorant for sensitive skin with fragrance-balanced formula, zero alcohol, and soothing chamomile extracts.',
    72.00, 98.00, 25, '/images/products/deodorant-6.png',
    23, false, false, true, 27, 'piece', 4.7,
    'deodorant,sensitive,gentle,care'
),
(
    'Ocean Fresh Mist',
    'Marine-inspired fragrance',
    'Long-lasting deodorant mist with oceanic scent profile, moisture-lock technology, and cooling sensation for hot climates.',
    39.00, 55.00, 45, '/images/products/deodorant-7.png',
    23, false, false, true, 29, 'piece', 4.4,
    'deodorant,ocean,mist,fresh'
),
(
    'Midnight Luxe Deo',
    'Luxury evening protection',
    'Sophisticated deodorant crafted with premium fragrance notes, 36-hour protection, and anti-stain technology for formal wear.',
    95.00, 130.00, 18, '/images/products/deodorant-8.png',
    23, false, false, true, 27, 'piece', 4.9,
    'deodorant,luxury,night,premium'
);

DELETE FROM products
WHERE id BETWEEN 222 AND 229;

select * from products;
select * from categories;

UPDATE categories SET gender = 'mens' WHERE id = 4;

-- Copy gender from category to product
UPDATE products p
JOIN categories c ON p.category_id = c.id
SET p.gender = c.gender;





-- Check total products
SELECT COUNT(*) as total FROM products;

-- Check gender distribution in products
SELECT gender, COUNT(*) as count FROM products GROUP BY gender;

-- Check products with NULL gender
SELECT COUNT(*) FROM products WHERE gender IS NULL;

-- Check if pagination is the issue — how many products per page should show
SELECT COUNT(*) FROM products WHERE gender = 'womens';
SELECT COUNT(*) FROM products WHERE gender = 'mens';
SELECT COUNT(*) FROM products WHERE gender = 'unisex';









INSERT INTO categories (name, slug, is_active) VALUES ('Electronics', 'electronics', 1);
UPDATE categories SET parent_id = 70 WHERE slug IN ('laptop', 'desktop', 'keyboard', 'mouse', 'tablet', 'mobile-phone', 'headphone', 'camera', 'smart-watch', 'smart-tv');

INSERT INTO categories (name, slug, is_active) VALUES ('Jewellery', 'jewellery', 1);
UPDATE categories SET parent_id = 73 WHERE slug IN ('earrings', 'couple-rings', 'necklace', 'bracelets');



ALTER TABLE categories ADD COLUMN gender VARCHAR(20) DEFAULT 'unisex';

-- Update based on existing category names
UPDATE categories SET gender = 'mens'     WHERE LOWER(name) LIKE '%men%' OR LOWER(name) LIKE '%man%' OR LOWER(slug) LIKE '%men%';
UPDATE categories SET gender = 'womens'   WHERE LOWER(name) LIKE '%women%' OR LOWER(name) LIKE '%woman%' OR LOWER(name) LIKE '%ladies%';
UPDATE categories SET gender = 'children' WHERE LOWER(name) LIKE '%kid%' OR LOWER(name) LIKE '%child%' OR LOWER(name) LIKE '%baby%';



SELECT id, name FROM products WHERE category_id = 5 ORDER BY id DESC LIMIT 3;