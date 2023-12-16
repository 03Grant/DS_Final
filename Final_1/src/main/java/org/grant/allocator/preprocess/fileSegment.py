import os
from lxml import etree


def count_records(file_path):
    """计算具有key属性的元素（记录）的总数"""
    count = 0
    for _, elem in etree.iterparse(file_path, events=('end',), tag='*', recover=True):
        if 'key' in elem.attrib:
            count += 1
        # 在完成处理后立即清除元素以节省内存
        elem.clear()
        # 同时清除祖先元素的引用，防止内存泄漏
        while elem.getprevious() is not None:
            del elem.getparent()[0]
    return count


def split_xml(file_path, output_dir, n_splits):
    """分割XML文件，每个文件包含大致相同数量的记录"""
    total_records = count_records(file_path)
    records_per_file = total_records // n_splits + 1
    current_file_count = 0
    current_record_count = 0
    current_file = None

    for event, elem in etree.iterparse(file_path, events=('end',), tag='*', recover=True):
        if 'key' in elem.attrib:
            # 创建一个新的element实例，保持相同的tag和属性
            new_elem = etree.Element(elem.tag, attrib=elem.attrib)

            # 仅复制author和year子元素及其文本内容
            for child in elem:
                if child.tag in ['author', 'year']:
                    new_child = etree.SubElement(new_elem, child.tag)
                    new_child.text = child.text  # 确保复制文本内容

            # 检查当前文件记录数
            if current_file is None or current_record_count >= records_per_file:
                if current_file is not None:
                    current_file.write(b'</dblp>\n')
                    current_file.close()

                current_file_path = os.path.join(output_dir, f'split_{current_file_count + 1}.xml')
                current_file = open(current_file_path, 'wb')
                current_file.write(b'<?xml version="1.0" encoding="UTF-8"?>\n')
                current_file.write(b'<dblp>\n')  # 假设根元素是'dblp'
                current_file_count += 1
                current_record_count = 0

            record = etree.tostring(new_elem, encoding='utf-8', pretty_print=True)
            current_file.write(record)
            current_record_count += 1

            elem.clear()
            while elem.getprevious() is not None:
                del elem.getparent()[0]

    if current_file is not None:
        current_file.write(b'</dblp>\n')
        current_file.close()


# Replace 'your_large_xml_file.xml' with the path to your large XML file.
# Specify the output directory where the split files should be saved.
# Specify the number of splits.
split_xml('C:\\Works\\大三\\分布式系统\\DS_Final\\test.xml', 'C:\\Works\\大三\\分布式系统\\DS_Final\\split_files', 10)
