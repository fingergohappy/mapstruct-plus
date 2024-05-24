/*
 * Copyright MapStruct Authors.
 *
 * Licensed under the Apache License version 2.0, available at http://www.apache.org/licenses/LICENSE-2.0
 */

package io.github.linpeilie;

import io.github.linpeilie.me.conditional.qualifier.Employee;
import io.github.linpeilie.me.conditional.qualifier.EmployeeDto;
import io.github.linpeilie.me.conditional.qualifier.Order;
import io.github.linpeilie.me.conditional.qualifier.OrderDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = Application.class)
public class ConditionalQualifierTest {

    @Autowired
    private Converter converter;

    @Test
    public void conditionalMethodWithSourceParameter() {
        EmployeeDto dto = new EmployeeDto();
        dto.setName("Tester");
        dto.setUniqueIdNumber("SSID-001");
        dto.setCountry(null);

        Employee employee = converter.convert(dto, Employee.class);
        assertThat(employee.getNin()).isNull();
        assertThat(employee.getSsid()).isNull();

        dto.setCountry("UK");
        employee = converter.convert(dto, Employee.class);
        assertThat(employee.getNin()).isEqualTo("SSID-001");
        assertThat(employee.getSsid()).isNull();

        dto.setCountry("US");
        employee = converter.convert(dto, Employee.class);
        assertThat(employee.getNin()).isNull();
        assertThat(employee.getSsid()).isEqualTo("SSID-001");

        dto.setCountry("CH");
        employee = converter.convert(dto, Employee.class);
        assertThat(employee.getNin()).isNull();
        assertThat(employee.getSsid()).isNull();
    }

    @Test
    public void conditionalClassQualifiers() {
        EmployeeDto dto = new EmployeeDto();
        dto.setName("Tester");
        dto.setUniqueIdNumber("SSID-001");
        dto.setCountry(null);

        Employee employee = converter.convert(dto, Employee.class);
        assertThat(employee.getNin()).isNull();
        assertThat(employee.getSsid()).isNull();

        dto.setCountry("UK");
        employee = converter.convert(dto, Employee.class);
        assertThat(employee.getNin()).isEqualTo("SSID-001");
        assertThat(employee.getSsid()).isNull();

        dto.setCountry("US");
        employee = converter.convert(dto, Employee.class);
        assertThat(employee.getNin()).isNull();
        assertThat(employee.getSsid()).isEqualTo("SSID-001");

        dto.setCountry("CH");
        employee = converter.convert(dto, Employee.class);
        assertThat(employee.getNin()).isNull();
        assertThat(employee.getSsid()).isNull();
    }

    @Test
    public void conditionalQualifiersForSourceToTarget() {
        OrderDTO orderDto = new OrderDTO();

        Order order = converter.convert(orderDto, Order.class);
        assertThat(order).isNotNull();
        assertThat(order.getCustomer()).isNull();

        orderDto = new OrderDTO();
        orderDto.setCustomerName("Tester");
        order = converter.convert(orderDto, Order.class);

        assertThat(order).isNotNull();
        assertThat(order.getCustomer()).isNotNull();
        assertThat(order.getCustomer().getName()).isEqualTo("Tester");
        assertThat(order.getCustomer().getAddress()).isNull();

        orderDto = new OrderDTO();
        orderDto.setLine1("Line 1");
        order = converter.convert(orderDto, Order.class);

        assertThat(order).isNotNull();
        assertThat(order.getCustomer()).isNotNull();
        assertThat(order.getCustomer().getName()).isNull();
        assertThat(order.getCustomer().getAddress()).isNotNull();
        assertThat(order.getCustomer().getAddress().getLine1()).isEqualTo("Line 1");
        assertThat(order.getCustomer().getAddress().getLine2()).isNull();

    }
}
