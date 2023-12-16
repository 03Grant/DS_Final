import os
from lxml import etree


def get_file_size(file_path):
    """获取文件大小（以字节为单位）"""
    return os.path.getsize(file_path)


def split_xml_by_size(file_path, output_dir, n_splits):
    """按文件大小均分XML文件"""
    total_size = get_file_size(file_path)
    target_size = total_size // n_splits + 1
    current_size = 0
    file_count = 0
    current_file = None

    for event, elem in etree.iterparse(file_path, events=('end',), tag='*', recover=True):
        if 'key' in elem.attrib:
            new_elem = etree.Element(elem.tag, attrib=elem.attrib)
            # 保留子元素
            for child in elem:
                new_child = etree.SubElement(new_elem, child.tag)
                new_child.text = child.text

            # 判断是否需要开始一个新文件
            if current_file is None or current_size >= target_size:
                if current_file is not None:
                    current_file.write(b'</dblp>\n')
                    current_file.close()

                current_file_path = os.path.join(output_dir, f'split_{file_count}.xml')
                current_file = open(current_file_path, 'wb')
                current_file.write(b'<?xml version="1.0" encoding="UTF-8"?>\n')
                current_file.write(b'<dblp>\n')  # 假设根元素是'dblp'
                file_count += 1
                current_size = len(b'<dblp>\n') + len(b'<?xml version="1.0" encoding="UTF-8"?>\n')

            # 写入当前记录
            record = etree.tostring(new_elem, encoding='utf-8', pretty_print=True)
            current_file.write(record)
            current_size += len(record)

        # 清除元素以节省内存
        # elem.clear()

    # 关闭最后一个文件
    if current_file is not None:
        current_file.write(b'</dblp>\n')
        current_file.close()


# 替换您的文件路径和输出目录
split_xml_by_size('C:\\Works\\大三\\分布式系统\\DS_Final\\dblp_trim.xml',    # split_files\\dblp_trim.xml',
                  'C:\\Works\\大三\\分布式系统\\DS_Final\\split_files', 7)  # 分割成10个文件
